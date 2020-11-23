package com.example.demopugspring.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demopugspring.engine.operation.AbstractOperation;
import com.example.demopugspring.factory.ContextSingleton;
import com.example.demopugspring.filter.MatchesValueFilter;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.model.Mapper.Category;
import com.example.demopugspring.operation.ClearFilteredOperation;
import com.example.demopugspring.operation.FieldOperation;
import com.example.demopugspring.operation.ReplaceOperation;
import com.example.demopugspring.operation.SwapOperation;
import com.example.demopugspring.properties.Codes;
import com.example.demopugspring.properties.CountryCodes;
import com.example.demopugspring.properties.FacilitiesCodes;
import com.example.demopugspring.properties.IdentificationCodes;
import com.example.demopugspring.properties.MarriageStatusCodes;
import com.example.demopugspring.properties.PropertiesCategoriesEnum;
import com.example.demopugspring.service.ApplicationService;
import com.example.demopugspring.service.IntegrationService;
import com.example.demopugspring.service.MessageService;
import com.example.demopugspring.visitor.MapperVisitor;
import com.example.demopugspring.visitor.TranscodingVisitor;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.datatype.CX;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

@Service
public class MapperEngine {

    private static final String OPERATIONS_PACKAGE = "operation";

    private static final Logger log = LoggerFactory.getLogger(MapperEngine.class);

    @Autowired
    IdentificationCodes identificationCodes;
    @Autowired
    FacilitiesCodes facilitiesCodes;
    @Autowired
    CountryCodes countryCodes;
    @Autowired
    MarriageStatusCodes marriageStatusCodes;
    @Autowired
    IntegrationService integrationService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    MessageService messageService;

    private void textFields(String field, String text, Terser encodedMessage, Terser decodedMessage) throws HL7Exception {
        MapperVisitor visitor;

        visitor = new MapperVisitor(field, text);
        visitor.start(decodedMessage.getSegment(field.split("-")[0]).getMessage());
    }


    public void transcode(Terser tmp, List<String> keys, String value, List<MapperError> errorList) throws HL7Exception {
        TranscodingVisitor transcodeVisitor;
        Codes codeInterface;
        PropertiesCategoriesEnum property = PropertiesCategoriesEnum.valueOfProperty(value);
        switch (property) {
            case FACILITIES:
                codeInterface = facilitiesCodes;
                break;
            case GH_LOCATIONS:
                codeInterface = countryCodes;
                break;
            case IDENTIFICATIONS:
                codeInterface = identificationCodes;
                break;
            case MARRIAGE_STATUS:
                codeInterface = marriageStatusCodes;
                break;
            default:
                throw new HL7Exception("Transcode propperty is incorrect.");
        }

        for (String key : keys) {
            transcodeVisitor = new TranscodingVisitor(key, value, codeInterface);
            transcodeVisitor.start(tmp.getSegment(key.split("-")[0]).getMessage());
        }


    }

    /**
     * @param msg
     * @param tmp
     * @param fields
     * @param value
     * @param type
     * @param errorList
     */
    void mapper(Terser msg, Terser tmp, List<String> fields, String value, Mapper.Category type, List<MapperError> errorList) {
        fields.forEach(field -> {
            try {
                if (field.contains("#")) {
                    boolean toContinue = true;
                    log.info("Contains # - loop all segments/fields");

                    int i = 0;

                    while (true) {

                        var fieldRep = field.replace("#", String.valueOf(i));

                        var valueRep = value;
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
                        break;
                    }
                } else {
                    log.info("No # on field - just a simple map");
                    switch (type) {
                        case TEXT:
                            tmp.set(field, value);
                            //textFields(field, value, msg, tmp);
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

    public void swapAfterOperation(Terser tmp, List<String> fields, String value, Mapper.Category type, List<MapperError> errorList) {
        SwapOperation swapOperation = new SwapOperation(value, fields);
        try {
            swapOperation.doOperation(tmp.getSegment(value.split("-")[0]).getMessage());
        } catch (HL7Exception e) {
            errorList.add(new MapperError(e.getError().name(), e.getDetail().toString()));
        }
    }

    public void fieldAfterOperation(Terser msg, Terser tmp, List<String> fields, String value, Mapper.Category type, List<MapperError> errorList) {
        FieldOperation fieldOperation = new FieldOperation(value, fields);
        try {
            fieldOperation.doOperation(tmp.getSegment(value.split("-")[0]).getMessage());
        } catch (HL7Exception e) {
            errorList.add(new MapperError(e.getError().name(), e.getDetail().toString()));
        }
    }

    public void clearIfOperation(Terser tmp, List<String> fields, String value, List<MapperError> errorList) {
        try {
            ClearFilteredOperation clearOperation = new ClearFilteredOperation(value, fields, new MatchesValueFilter(value, tmp));
            clearOperation.doOperation(tmp.getSegment(fields.get(0).split("-")[0]).getMessage());
        } catch (HL7Exception e) {
            errorList.add(new MapperError(e.getError().name(), e.getDetail().toString()));
        }
    }

    public void replaceOperation(Terser tmp, List<String> fields, String value, List<MapperError> errorList) {
        ReplaceOperation replaceOperation = new ReplaceOperation(value, fields);
        try {
            replaceOperation.doOperation(tmp.getSegment(fields.get(0).split("-")[0]).getMessage());
        } catch (HL7Exception e) {
            errorList.add(new MapperError(e.getError().name(), e.getDetail().toString()));
        }
    }

    public void addContactRepetitions(Terser tmp, String field, String... strings) throws HL7Exception {
        int i = 0;

        for (String s : strings) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            boolean isPhone = s.matches("[\\d]+");
            if (isPhone) {
                tmp.set(field + "-13(" + i + ")-3", "PH");
                tmp.set(field + "-13(" + i + ")-12", s);
                tmp.set(field + "-13(" + i + ")-4", "");
                tmp.set(field + "-13(" + i + ")-7", "");

            } else {
                tmp.set(field + "-13(" + i + ")-3", "X.400");
                tmp.set(field + "-13(" + i + ")-4", s);
                tmp.set(field + "-13(" + i + ")-12", "");
            }
            tmp.set(field + "-14-7", "");

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
            // Transforming the string before parsing to a HL7v2 Message
            incomingMessage
                    .replace("PDF_BASE64", "ED")
                    .replace("|JVBER", "|^^^^JVBER");
            Message message = parser.parse(incomingMessage);
            Message volatileMessage = parser.parse(incomingMessage);
            log.info("Incoming message version:" + message.getVersion());
            Terser msg = new Terser(message);
            Terser volatileTerser = new Terser(volatileMessage);
            String messageCode = msg.get("MSH-9-1");
            String messageEvent = msg.get("MSH-9-2");
            String sendingApp = msg.get("MSH-3-1");
            String receivingApp = msg.get("MSH-5-1");
            String messageVersion = msg.get("MSH-12");

            Integration integration = integrationService.findByMessageAndApplications(
                    messageService.find(messageCode, messageEvent, messageVersion),
                    applicationService.findByCode(sendingApp),
                    applicationService.findByCode(receivingApp));
            if (integration == null) {
                throw new HL7Exception("No integration found for message " + messageCode + "-" + messageEvent +
                        " and sending application " + sendingApp + " and receiving application " + receivingApp);
            }
            log.info("Integration:" + integration.getMappers().toString());
            // Change message version
            volatileTerser.set("MSH-9-1", integration.getResultMessage().getCode());
            volatileTerser.set("MSH-9-2", integration.getResultMessage().getEvent());
            volatileTerser.set("MSH-12", integration.getResultMessage().getVersion().getValue());
            Message outMessage = parser.parse(volatileMessage.encode());
            log.info(outMessage.getName() + " " + outMessage.getVersion());
            for (String name : outMessage.getNames()) {
                log.info(name);
            }
            Terser tmp = new Terser(outMessage);
            for (Mapper mapper : integration.getMappers()) {
                Category mapperCategory = mapper.getCategory();
                List<Category> supportedOperations = Arrays.asList(Category.TEXT);
                if (supportedOperations.contains(mapperCategory)) {
                    String mapperClassName = this.getClass().getPackageName() + "." + OPERATIONS_PACKAGE + "." + mapperCategory.getValue();

                    AbstractOperation mapperInstance = null;
                    try {
                        log.debug("Searching for class " + mapperClassName + "...");
                        @SuppressWarnings("unchecked")
                        Class<? extends AbstractOperation> mapperClass = (Class<? extends AbstractOperation>) Class.forName(mapperClassName);

                        Constructor<? extends AbstractOperation> mapperConstructor = mapperClass.getConstructor(this.getClass(), Message.class, Message.class,
                                Terser.class, Terser.class, List.class, String.class);

                        mapperInstance = mapperConstructor.newInstance(this, message, outMessage, msg, tmp, mapper.getKey(), mapper.getValue());
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
                        mapperInstance.map();
                        errorList.addAll(mapperInstance.getErrors());
                    } catch (Exception e) {
                        log.error("Unexpected exception running mapper '" + mapperClassName + "'!", e);
                        errorList.add(new MapperError("Global", "Unexpected exception running mapper '" + mapperClassName + "'!"));
                    }
                } else {
                    switch (mapperCategory) {
                        case TEXT:
                        case FIELD:
                        case SWAP:
                        case SEGMENT:
                        case JOIN:
                        case NUMERIC:
                            log.info("TEXT or FIELD");
                            mapper(msg, tmp, mapper.getKey(), mapper.getValue(), mapperCategory, errorList);
                            break;
                        case CONTACT:
                            String field = mapper.getKey().get(0);
                            addContactRepetitions(tmp, field, tmp.get(field + "-13-7-1"), tmp.get(field + "-13-12-1"), tmp.get(field + "-14-7-1"), tmp.get(field + "-14-12-1"), tmp.get(field + "-13-04"));
                            break;
                        case ADD_SNS:
                            addFieldSNS(tmp, msg, mapper.getKey(), mapper.getValue(), errorList);
                            break;
                        case AFTER_JOIN_FIELDS:
                            joinFields(tmp, mapper.getKey(), mapper.getValue(), errorList);
                            break;
                        case AFTER_FIELD:
                            fieldAfterOperation(msg, tmp, mapper.getKey(), mapper.getValue(), mapperCategory, errorList);
                            break;
                        case AFTER_SWAP:
                            swapAfterOperation(tmp, mapper.getKey(), mapper.getValue(), mapperCategory, errorList);
                            break;
                        case TRANSCODING:
                            transcode(tmp, mapper.getKey(), mapper.getValue(), errorList);
                            break;
                        case CLEAR_IF:
                            clearIfOperation(tmp, mapper.getKey(), mapper.getValue(), errorList);
                            break;
                        case REPLACE:
                            replaceOperation(tmp, mapper.getKey(), mapper.getValue(), errorList);
                            break;
                        default:
                            errorList.add(new MapperError(mapper.getKey().toString(), "No Category: " + mapperCategory));
                    }
                }
            }
            log.info("Out message version:" + outMessage.getName() + " " + outMessage.getVersion());
            result = outMessage.encode();
            result = cleanMessage(result);
            log.info(result);
        } catch (HL7Exception ex) {
            log.error(ex.getMessage());
            errorList.add(new MapperError("Global", ex.getMessage()));
        }
        response.setMessage(result);
        response.setErrorList(errorList);
        return response;
    }

    public void joinFields(Terser tmp, List<String> key, String value, List<MapperError> errorList) throws HL7Exception {
        String[] field_split = key.get(0).split("-");
        String[] value_split = value.split("-");
        Segment segmentTarget = tmp.getSegment(field_split[0]);

        int numberRepTarget = tmp.getSegment(field_split[0]).getField(Integer.valueOf(field_split[1])).length;
        int numberRepSource = tmp.getSegment(value_split[0]).getField(Integer.valueOf(value_split[1])).length;
        int totalRepetitions = (numberRepTarget + numberRepSource);
        int indexRepTarget = numberRepTarget;
        int indexRepSource = 0;

        while (indexRepTarget < totalRepetitions) {
            while (indexRepSource < numberRepSource) {
                int indexFields = 1;
                int numFieldsSource = ((CX) segmentTarget.getField(Integer.valueOf(field_split[1]), indexRepSource)).getComponents().length;
                while (indexFields < numFieldsSource + 1) {
                    String valueToPass = Terser.get(segmentTarget, Integer.valueOf(value_split[1]), indexRepSource, indexFields, 1);
                    Terser.set(segmentTarget, Integer.valueOf(field_split[1]), indexRepTarget, indexFields, 1, valueToPass);
                    Terser.set(tmp.getSegment(value_split[0]), Integer.valueOf(value_split[1]), indexRepSource, indexFields, 1, null);
                    indexFields++;
                }
                indexRepTarget++;
                indexRepSource++;
            }

        }
    }

    public void addFieldSNS(Terser tmp, Terser msg, List<String> key, String value, List<MapperError> errorList) throws HL7Exception {

        String[] field_split = key.get(0).split("-");
        Segment segmentTarget = tmp.getSegment(field_split[0]);

        int numberRepTarget = tmp.getSegment(field_split[0]).getField(Integer.valueOf(field_split[1])).length;
        Terser.set(segmentTarget, Integer.valueOf(field_split[1]), numberRepTarget, 1, 1, msg.get(value));
        Terser.set(segmentTarget, Integer.valueOf(field_split[1]), numberRepTarget, 4, 1, "SNS");
    }

    private String cleanMessage(String message) {
        return message.replaceAll("\\|(~)*\\|", "||");
    }


}
