package com.example.demopugspring.engine;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import com.example.demopugspring.controller.IntegrationRestController;
import com.example.demopugspring.factory.ContextSingleton;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.service.ApplicationService;
import com.example.demopugspring.service.IntegrationService;
import com.example.demopugspring.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapperEngine {

    private static final Logger log = LoggerFactory.getLogger(IntegrationRestController.class);

    @Autowired
    IntegrationService integrationService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    MessageService messageService;

    /*
    private static String v24message = "MSH|^~\\&|GH|HCIS|DOTLOGIC|HCIS|20200709192254||OMG^O19|37272407|P|2.4|||AL\r"
            + "NTE|||S|TIPO_ENVIO_RESULTADOS^TIPO_ENVIO_RESULTADOS^TIPO_ENVIO_RESULTADOS\r"
            + "NTE|||N|DESTINO_ENVIO_RESULTADOS^DESTINO_ENVIO_RESULTADOS^DESTINO_ENVIO_RESULTADOS\r"
            + "PID|||9967492^^^JMS^NS~1380466^^^HCIS^NS~271487^^^CCA^NS~302905^^^CCTV^NS|141342838^^^NIF^PT~1856391111^^^N_BENEF~07271576^^^N_BI|RIBEIRO^ANA MARIA ANTUNES DOS SANTOS MENINO||19660524000000|F|||RUA HELENA VAZ DA SILVA, 10, 1º C - ALTA DE LISBOA^^LISBOA^11^CP 1750-432^PORTUGAL||^^^anamenino@gmail.com^^^931717817|^^^^^^962363614|||||370620506||||PORTUGAL|||||^PORTUGAL||N\r"
            + "PV1||Consultas|9^HOS-1C7^^^^^^^CARDIOLOGIA|S||9^^^^^^^^CARDIOLOGIA|||15225^Ramos^Sousa|9||||CON||N|5000305||11959318||1924|S||||||||||||||||||||||20200709000000||||||B0077^^^^GA_NUM_SENHA\r"
            + "ORC|SC|5926450||11959318|CA||1.000||20200709192250|270680187^Geraldes^Ines Isabel da Cunha Lima|||HOS-1C7\r"
            + "OBR|1|5926450||9000003^ECG SIMPLES||20200709192250|20200709191513||||||||||||||||||||1^^^20200709000000|||||5000305&Ramos&Sousa||||20200709191513";
*/

    private void transcode(Terser terser, List<String> field, String system) throws HL7Exception {
        System.out.println("System:" + system);
        switch (system) {
            case "ICD-10":
                System.out.println("ICD-10");
                System.out.println(field);
                terser.set(field.get(0), "1");
                terser.set(field.get(1), "UM");
                terser.set(field.get(2), "ISO");
                break;
            case "GH-LOCATIONS":
                System.out.println("GH-LOCATIONS");
                System.out.println(field);
                terser.set(field.get(0), "cardiology");
                terser.set(field.get(1), "CARDIOLOGY");
                break;
            default:
                log.error("No defined code system.");
        }
    }

    private void mapper(Terser terser, List<String> fields, String value, Mapper.Category type) {
        fields.forEach(field -> {
            try {
                if (field.contains("#")) {
                    log.info("Contains # - loop all segments/fields");
                    int i = 0;
                    while (true) {
                        var fieldRep = field.replace("#", String.valueOf(i));
                        var valueRep = "";
                        if (value.equals("#")) {
                            valueRep = String.valueOf(i + 1);
                        } else if (type == Mapper.Category.FIELD) {
                            valueRep = value.replace("#", i + "");
                        }
                        log.info(fieldRep);
                        log.info(valueRep);
                        if (terser.getSegment(fieldRep).isEmpty()) {
                            log.info("Segmento:" + terser.getSegment(fieldRep).encode());
                            log.info("Segment is empty.");
                            break;
                        }
                        switch (type) {
                            case TEXT:
                                terser.set(fieldRep, valueRep);
                                break;
                            case FIELD:
                                terser.set(fieldRep, terser.get(valueRep));
                                break;
                            case SWAP:
                                String tmp = terser.get(fieldRep);
                                terser.set(fieldRep, terser.get(valueRep));
                                terser.set(valueRep, tmp);
                                break;
                            default:
                                log.error("No defined Category");
                        }
                        i++;
                    }
                } else {
                    log.info("No # on field - just a simple map");
                    switch (type) {
                        case TEXT:
                            terser.set(field, value);
                            break;
                        case FIELD:
                            terser.set(field, terser.get(value));
                            break;
                        case SWAP:
                            String tmp = terser.get(field);
                            terser.set(field, terser.get(value));
                            terser.set(value, tmp);
                            break;
                        default:
                            log.error("No defined category");
                    }
                }
            } catch (HL7Exception ex) {
                log.error("Error on HL7 mapping", ex);
            }
        });
    }

    public String invoke(String msg) throws HL7Exception {
        HapiContext context = ContextSingleton.getInstance();
        PipeParser parser = context.getPipeParser();
        Message message = parser.parse(msg);
        log.info(message.encode());
        Terser terser = new Terser(message);
        String messageCode = terser.get("MSH-9-1");
        String messageEvent = terser.get("MSH-9-2");
        String sendingApp = terser.get("MSH-3-1");
        String receivingApp = terser.get("MSH-5-1");
        log.info("PV1-2:" + terser.get(".PV1-2"));
        Integration integration = integrationService.findByMessageAndApplications(
                messageService.findByCodeAndEvent(messageCode, messageEvent),
                applicationService.findByCode(sendingApp),
                applicationService.findByCode(receivingApp));
        log.info("Integration:" + integration.getMappers().toString());

        for (Mapper mapper : integration.getMappers()) {
            switch (mapper.getCategory()) {
                case TEXT:
                case FIELD:
                case SWAP:
                    log.info("TEXT or FIELD");
                    mapper(terser, mapper.getKey(), mapper.getValue(), mapper.getCategory());
                    break;
                case TRANSCODING:
                    transcode(terser, mapper.getKey(), mapper.getValue());
                    break;
                default:
                    throw (new HL7Exception("TESTE"));
            }
        }
        String result = message.encode();
        log.info(result);
        return result;
    }

}
