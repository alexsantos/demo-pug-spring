package com.example.demopugspring.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demopugspring.controller.IntegrationRestController;
import com.example.demopugspring.engine.mappers.AbstractMapper;
import com.example.demopugspring.factory.ContextSingleton;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.model.Mapper.Category;
import com.example.demopugspring.properties.CodesInterface;
import com.example.demopugspring.properties.CountryCodes;
import com.example.demopugspring.properties.FacilitiesCodes;
import com.example.demopugspring.service.ApplicationService;
import com.example.demopugspring.service.IntegrationService;
import com.example.demopugspring.service.MessageService;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

@Service
public class MapperEngine {

    private static final Logger log = LoggerFactory.getLogger(IntegrationRestController.class);

	private static final String MAPPERS_PACKAGE = "mappers";

	@Autowired
	FacilitiesCodes facilitiesCodes;

	@Autowired
	CountryCodes countryCodes;

    @Autowired
    IntegrationService integrationService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    MessageService messageService;

	public FacilitiesCodes getFacilitiesCodes() {
		return facilitiesCodes;
	}
	
	public CountryCodes getCountryCodes() {
		return countryCodes;
	}
    /*
    private static String v24message = "MSH|^~\\&|GH|HCIS|DOTLOGIC|HCIS|20200709192254||OMG^O19|37272407|P|2.4|||AL\r"
            + "NTE|||S|TIPO_ENVIO_RESULTADOS^TIPO_ENVIO_RESULTADOS^TIPO_ENVIO_RESULTADOS\r"
            + "NTE|||N|DESTINO_ENVIO_RESULTADOS^DESTINO_ENVIO_RESULTADOS^DESTINO_ENVIO_RESULTADOS\r"
            + "PID|||9967492^^^JMS^NS~1380466^^^HCIS^NS~271487^^^CCA^NS~302905^^^CCTV^NS|141342838^^^NIF^PT~1856391111^^^N_BENEF~07271576^^^N_BI|RIBEIRO^ANA MARIA ANTUNES DOS SANTOS MENINO||19660524000000|F|||RUA HELENA VAZ DA SILVA, 10, 1º C - ALTA DE LISBOA^^LISBOA^11^CP 1750-432^PORTUGAL||^^^anamenino@gmail.com^^^931717817|^^^^^^962363614|||||370620506||||PORTUGAL|||||^PORTUGAL||N\r"
            + "PV1||Consultas|9^HOS-1C7^^^^^^^CARDIOLOGIA|S||9^^^^^^^^CARDIOLOGIA|||15225^Ramos^Sousa|9||||CON||N|5000305||11959318||1924|S||||||||||||||||||||||20200709000000||||||B0077^^^^GA_NUM_SENHA\r"
            + "ORC|SC|5926450||11959318|CA||1.000||20200709192250|270680187^Geraldes^Ines Isabel da Cunha Lima|||HOS-1C7\r"
            + "OBR|1|5926450||9000003^ECG SIMPLES||20200709192250|20200709191513||||||||||||||||||||1^^^20200709000000|||||5000305&Ramos&Sousa||||20200709191513";
*/
	private void transcode(Terser msg, Terser tmp, List<String> fields, String system, List<MapperError> errorList) {
        System.out.println("System:" + system);
        try {

            switch (system) {
                case "ICD-10":
                    System.out.println("ICD-10");
					System.out.println(fields);
					tmp.set(fields.get(0), "1");
					tmp.set(fields.get(1), "UM");
					tmp.set(fields.get(2), "ISO");
                    break;
                case "GH-LOCATIONS":
					decodeFieldsCodes(fields, countryCodes, msg, tmp);
                    break;
				case "FACILITIES":
					decodeFieldsCodes(fields, facilitiesCodes, msg, tmp);
					break;
                default:
                    log.error("No defined code system.");
					errorList.add(new MapperError(fields.toString(), "No defined code system: " + system));
            }
        } catch (HL7Exception ex) {
            log.error(ex.getMessage());
			errorList.add(new MapperError(fields.toString(), ex.getMessage()));
        }
    }

	private void decodeFieldsCodes(List<String> fields, CodesInterface codeInterface, Terser encodedMessage, Terser decodedMessage) throws HL7Exception {
		for (String field : fields) {
			decodedMessage.set(field, codeInterface.getDecodeCode(encodedMessage.get(field)));
		}
	}

    private void mapper(Terser msg, Terser tmp, List<String> fields, String value, Mapper.Category type, List<MapperError> errorList) {
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
                        if (msg.getSegment(fieldRep).isEmpty()) {
                            log.info("Segmento:" + msg.getSegment(fieldRep).encode());
                            log.info("Segment is empty.");
                            break;
                        }
                        switch (type) {
                            case TEXT:
                                tmp.set(fieldRep, valueRep);
                                break;
                            case FIELD:
                                tmp.set(fieldRep, msg.get(valueRep));
                                break;
                            case SWAP:
							    tmp.set(fieldRep, msg.get(valueRep));
							    tmp.set(valueRep, msg.get(fieldRep));
                                break;
                            default:
                                log.error("No defined Category");
                                errorList.add(new MapperError(field, "No Category defined as: " + type));
                        }
                        i++;
                    }
                } else {
                    log.info("No # on field - just a simple map");
					switch (type) {
					case TEXT:
                            tmp.set(field, value);
                            break;
					case FIELD:
                            tmp.set(field, msg.get(value));
                            break;
                        case SWAP:
						    tmp.set(field, msg.get(value));
						    tmp.set(value, msg.get(field));
                            break;
					case SEGMENT:
                            tmp.getSegment(field).parse(field);
                            break;
					case JOIN:
                            StringBuilder joined = new StringBuilder();
                            log.info("Fields to join:" + value);
                            for (String val : value.split(",")) {
                                log.info("Value for " + val + ":" + msg.get(val));
                                joined.append(msg.get(val));
                            }
                            log.info("joined fields:" + joined.toString());
                            tmp.set(field, joined.toString());
                            break;
					case NUMERIC:
                            tmp.set(field, msg.get(field).replaceAll("[^\\d.]", ""));
                            break;
					case CONTACT:
						addRepetitions(tmp, tmp.get("PID-13-12-1"), tmp.get("PID-14-7-1"), tmp.get("PID-13-4"));
						break;
					default:
						log.error("No defined category");
						errorList.add(new MapperError(field, "No Category defined as " + type));
                    }
                }
            } catch (HL7Exception ex) {
                log.error("Error on HL7 mapping", ex);
                errorList.add(new MapperError(field, ex.getMessage()));
            }
        });
    }

	private void swapAfterOperarion(Terser msg, Terser tmp, List<String> fields, String value, Mapper.Category type, List<MapperError> errorList) throws HL7Exception {
		String firstValue, secondValue;
		for (String field : fields) {
			firstValue = tmp.get(value);
			secondValue = tmp.get(field);
			tmp.set(field, firstValue);
			tmp.set(value, secondValue);
		}
	}


	public void addRepetitions(Terser tmp, String... strings) throws HL7Exception {
		StringBuffer listContactsHome = new StringBuffer();
		int i=0;
		for (String s : strings) {
			if (StringUtils.isEmpty(s)) {
				continue;
			}
			boolean isPhone = s.matches("[\\d]+");
			if (isPhone) {
				tmp.set("PID-13("+i+")-3", "PH");
				tmp.set("PID-13("+i+")-12", s);
				tmp.set("PID-13(" + i + ")-4", "");
			} else {
				tmp.set("PID-13("+i+")-3", "X.400");
				tmp.set("PID-13(" + i + ")-4", s);
				tmp.set("PID-13(" + i + ")-12", "");
			}
			tmp.set("PID-14(" + i + ")-7", "");

			i++;
		}
	}

	public Response run(String incomingMessage) {
        String result = "";
        Response response = new Response();
        List<MapperError> errorList = new ArrayList<>();
        HapiContext context = ContextSingleton.getInstance();
        PipeParser parser = context.getPipeParser();
        try {
			Message inMessage = parser.parse(incomingMessage);
			// Message outMessage = new GenericMessage.V251(new
			// GenericModelClassFactory());
			// outMessage.parse(incomingMessage);
            Message outMessage = parser.parse(incomingMessage);
			log.info("Incoming message version:" + inMessage.getVersion());
			Terser inTerser = new Terser(inMessage);
			Terser outTerser = new Terser(outMessage);
			String messageCode = inTerser.get("MSH-9-1");
			String messageEvent = inTerser.get("MSH-9-2");
			String sendingApp = inTerser.get("MSH-3-1");
			String receivingApp = inTerser.get("MSH-5-1");
			log.info("PV1-2:" + inTerser.get(".PV1-2"));

            Integration integration = integrationService.findByMessageAndApplications(
                    messageService.findByCodeAndEvent(messageCode, messageEvent),
                    applicationService.findByCode(sendingApp),
                    applicationService.findByCode(receivingApp));
            if (integration == null) {
                throw new HL7Exception("No integration found for message " + messageCode + "-" + messageEvent +
                        " and sending application " + sendingApp + " and receiving application " + receivingApp);
            }
            log.info("Integration:" + integration.getMappers().toString());
            // Change message version

            for (Mapper mapper : integration.getMappers()) {
				Category mapperCategory = mapper.getCategory();
				String mapperClassName = this.getClass().getPackageName() + "." + MAPPERS_PACKAGE + "." + mapperCategory.getValue();
				
				AbstractMapper mapperInstance;
				try {
					log.debug("Searching for class " + mapperClassName + "...");
					@SuppressWarnings("unchecked")
					Class<? extends AbstractMapper> mapperClass = (Class<? extends AbstractMapper>) Class.forName(mapperClassName);

					Constructor<? extends AbstractMapper> mapperConstructor = mapperClass.getConstructor(new Class[] { this.getClass(), Message.class, Message.class,
							Terser.class, Terser.class, List.class, String.class });

					mapperInstance = mapperConstructor.newInstance(this, inMessage, outMessage, inTerser, outTerser, mapper.getKey(), mapper.getValue());
				} catch (ClassNotFoundException e) {
					log.error("Couldn't find class for Mapper category " + mapperCategory + ": " + mapperClassName + "!", e);
					errorList.add(new MapperError("Global", "Mapper category " + mapperCategory + " isn't supported!"));
					continue;
				} catch (NoSuchMethodException | SecurityException e) {
					log.error("Couldn't find right constructor for " + mapperClassName + "!", e);
					errorList.add(new MapperError("Global", "Mapper category " + mapperCategory + " isn't supported!"));
					continue;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Couldn't instantiate " + mapperClassName + "!", e);
					errorList.add(new MapperError("Global", "Mapper category " + mapperCategory + " isn't supported!"));
					continue;
				}
				
				try {
					errorList.addAll(mapperInstance.map());
				}
				catch (Exception e) {
					log.error("Unexpected exception running mapper '" + mapperClassName + "'!", e);
					errorList.add(new MapperError("Global", "Unexpected exception running mapper '" + mapperClassName + "'!"));
				}
            }
            log.info("Out message version:" + outMessage.getVersion());
            result = outMessage.encode();
            log.info(result);
        } catch (HL7Exception ex) {
            log.error(ex.getMessage());
            errorList.add(new MapperError("Global", ex.getMessage()));
        }
        response.setMessage(result);
        response.setErrorList(errorList);
        return response;
    }

}
