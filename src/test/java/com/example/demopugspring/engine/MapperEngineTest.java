package com.example.demopugspring.engine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.example.demopugspring.factory.ContextSingleton;
import com.example.demopugspring.model.Mapper.Category;
import com.example.demopugspring.properties.CountryCodes;
import com.example.demopugspring.properties.IdentificationCodes;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

class MapperEngineTest {
	@Mock
	List<MapperError> errors;


	@Test
	void testReplace() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||198212090000|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";

		String toBeReturned = "19821209";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/PID-7", "/PID-13-7"};
		meng.replaceOperation(terserSpy, Arrays.asList(testeListaKeys), "(00)*", errors);
		assertEquals(toBeReturned, terserSpy.get("/PID-7"));
		assertEquals("9", terserSpy.get("/PID-13-7"));

	}

	@Test
	void testReplaceEmpty() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO|||M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";

		String toBeReturned = "19821209";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/PID-7" };
		meng.replaceOperation(terserSpy, Arrays.asList(testeListaKeys), "(00)*", errors);
		assertEquals("", terserSpy.get("/PID-7"));
	}

	@Test
	void testAddSNSOperation() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";
		String toBeReturned = "1234512345";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		Mockito.doReturn(toBeReturned).when(terserSpy).get(Mockito.contains("/PID-19"));


		//outMessage.get(name, rep)
		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/PID-3"};
		meng.addFieldSNS(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "/PID-19", errors);
		assertEquals(toBeReturned, terserSpy.get("PID-3(2)"));
		assertEquals("SNS", terserSpy.get("PID-3(2)-4"));
	}

	@Test
	void testAddContact() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^teste@teste.com^^^999999999|^^^^^^222222222||||||||||||||1^PORTUGAL||N\r";
		String toBeReturnedTelf = "999999999";
		String toBeReturnedDois = "222222222";
		String toBeReturnedEmail = "teste@teste.com";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		t.get("/PID" + "-13-12");
		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/PID"};
		String field = testeListaKeys[0];
		meng.addContactRepetitions(terserSpy, field, terserSpy.get(field + "-13-7"), terserSpy.get(field + "-13-12"), terserSpy.get(field + "-14-7"), terserSpy.get(field + "-13-4"));
		assertEquals("X.400", terserSpy.get("PID-13(2)-3"));
		assertEquals(toBeReturnedEmail, terserSpy.get("PID-13(2)-4"));
		assertEquals(toBeReturnedTelf, terserSpy.get("PID-13(0)-12"));
		assertEquals(toBeReturnedDois, terserSpy.get("PID-13(1)-12"));


	}

	@Test
	void testClearIfOperation() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";
		String toFilter = "CUFC";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);


		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/PID-3(#)-4"};
		meng.clearIfOperation(terserSpy, Arrays.asList(testeListaKeys), toFilter, errors);
		assertEquals(null, terserSpy.get("PID-3(1)-1"));
	}

	@Test
	void testClearIfOperationPatient() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";
		String toFilter = "CUFC";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/PATIENT/PID-3(#)-4"};
		meng.clearIfOperation(terserSpy, Arrays.asList(testeListaKeys), toFilter, errors);
		assertEquals(null, terserSpy.get("/PATIENT/PID-3(1)-1"));
	}


	@Test
	void testAddSNSOperationPatient() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";
		String toBeReturned = "1234512345";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		Mockito.doReturn(toBeReturned).when(terserSpy).get(Mockito.contains("/PATIENT/PID-19"));

		List<MapperError> errors = Mockito.mock(List.class);
		// outMessage.get(name, rep)
		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/PATIENT/PID-3"};
		meng.addFieldSNS(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "/PATIENT/PID-19", errors);
		assertEquals(toBeReturned, terserSpy.get("/PATIENT/PID-3(2)"));
		assertEquals("SNS", terserSpy.get("/PATIENT/PID-3(2)-4"));
	}

	@Test
	void testTranscoding() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";
		String toBeReturned = "PT";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		List<MapperError> errors = Mockito.mock(List.class);
		MapperEngine meng = new MapperEngine();
		MapperEngine mengSpy = Mockito.spy(meng);

		CountryCodes codes = Mockito.mock(CountryCodes.class);
		Mockito.doReturn(toBeReturned).when(codes).getDecodeCode(Mockito.contains("1"));

		String[] testeListaKeys = {"/PID-28"};
		meng.countryCodes = codes;
		meng.transcode(terserSpy, Arrays.asList(testeListaKeys), "GH-LOCATIONS", errors);
		assertEquals(toBeReturned, terserSpy.get("/PID-28-1"));
	}

	@Test
	void testTranscodingPatient() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";
		String toBeReturned = "PT";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		List<MapperError> errors = Mockito.mock(List.class);
		MapperEngine meng = new MapperEngine();
		MapperEngine mengSpy = Mockito.spy(meng);

		CountryCodes codes = Mockito.mock(CountryCodes.class);
		Mockito.doReturn(toBeReturned).when(codes).getDecodeCode(Mockito.contains("1"));

		String[] testeListaKeys = {"/PATIENT/PID-28"};
		meng.countryCodes = codes;
		meng.transcode(terserSpy, Arrays.asList(testeListaKeys), "GH-LOCATIONS", errors);
		assertEquals(toBeReturned, terserSpy.get("/PATIENT/PID-28-1"));
	}

	@Test
	void testTranscodingRepetion() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";
		String toBeReturned = "PT";
		String toBeReturned2 = "NI";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		List<MapperError> errors = Mockito.mock(List.class);
		MapperEngine meng = new MapperEngine();
		MapperEngine mengSpy = Mockito.spy(meng);

		IdentificationCodes codes = Mockito.mock(IdentificationCodes.class);
		Mockito.doReturn(toBeReturned).when(codes).getDecodeCode(Mockito.contains("JMS"));
		Mockito.doReturn(toBeReturned2).when(codes).getDecodeCode(Mockito.contains("CUFC"));

		String[] testeListaKeys = {"/PID-3(#)-4"};
		meng.identificationCodes = codes;
		meng.transcode(terserSpy, Arrays.asList(testeListaKeys), "IDENTIFICATIONS", errors);
		assertEquals(toBeReturned, terserSpy.get("/PID-3(0)-4"));
		assertEquals(toBeReturned2, terserSpy.get("/PID-3(1)-4"));

	}

	@Test
	void testAfterField() throws HL7Exception {
		String toBeReturned = "CUFC";
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|TESTE|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|" + toBeReturned + "^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/MSH-6" };
		meng.fieldAfterOperation(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "/MSH-4", Category.AFTER_FIELD, errors);
		assertEquals(toBeReturned, terserSpy.get("/MSH-6"));
	}

	@Test
	void testTranscodingEmpty() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";
		String toBeReturned = null;
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		List<MapperError> errors = Mockito.mock(List.class);
		MapperEngine meng = new MapperEngine();
		MapperEngine mengSpy = Mockito.spy(meng);

		CountryCodes codes = Mockito.mock(CountryCodes.class);
		Mockito.doReturn(toBeReturned).when(codes).getDecodeCode(Mockito.contains("1"));

		String[] testeListaKeys = { "/PID-23" };
		meng.countryCodes = codes;
		meng.transcode(terserSpy, Arrays.asList(testeListaKeys), "GH-LOCATIONS", errors);
		assertEquals(toBeReturned, terserSpy.get("/PID-23-1"));
	}

	@Test
	void testReplaceOpearion() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CCA|ehCOS|CCA|20201112103816||ADT^A31|1601940378|P|2.4|||AL\r"
				+ "PID|||59594292^^^JMS^NS~1234^^^CCA^NS~1234553^^^HCD^NS~883885^^^CUFP^NS~123456789^^^NIF^PT~22810278800^^^N_BENEF^~32169833^^^N_BI^||TESTE^JULIA BABO CORREIA||19910302000000|F|||RUA DO 4 DE TESTE, N\\XBA\\ 88, 3\\XBA\\ DTO^^LISBOA^11^1350-275^1||^^PH^^^^^^^^^999999999~^^X.400^juliap@gmail.com|||U|||999999999||||PORTUGAL|||||1^PORTUGAL||N\r";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		List<MapperError> errors = Mockito.mock(List.class);
		MapperEngine meng = new MapperEngine();

		String[] testeListaKeys = { "/PID-7" };
		meng.replaceOperation(terserSpy, Arrays.asList(testeListaKeys), "(00)+", errors);

		assertEquals("19910302", terserSpy.get("/PID-7"));
	}


	@Test
	void testJoinFieldsPatient() throws HL7Exception {
		String toBeReturned = "995896186";
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/PATIENT/PID-3"};
		meng.joinFields(terserSpy, Arrays.asList(testeListaKeys), "/PATIENT/PID-4", errors);
		assertEquals(toBeReturned, terserSpy.get("/PATIENT/PID-3(2)"));
		assertEquals("NIF", terserSpy.get("/PATIENT/PID-3(2)-4"));

	}

	@Test
	void testField() throws HL7Exception {
		String toBeReturned = "CUFC";
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|TESTE|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|" + toBeReturned + "^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/MSH-6"};
		meng.mapper(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "/MSH-4", Category.FIELD, errors);
		assertEquals(toBeReturned, terserSpy.get("/MSH-6"));

	}

	@Test
	void testText() throws HL7Exception {
		String toBeReturned = "INTEGRACAO";
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|TESTE|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|" + toBeReturned + "^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = {"/MSH-6"};
		meng.mapper(terserSpy, terserSpy, Arrays.asList(testeListaKeys), toBeReturned, Category.TEXT, errors);
		assertEquals(toBeReturned, terserSpy.get("/MSH-6"));

	}

	@Test
	void testFixMessage() {
		String messageString = "MSH|^~\\&|CWM|CCTV|GH|CCB|20201103160113||ORU^R01|7ba75e23-f933-4c7f-b5ed-2be0efccc588|P|2.4|||AL\r"
				+ "PID||43417401|43417401^^^JMS^NS|12586669^^^N_BI|CUNHA^LETÍCIA SOFIA^||19830224|F|||RUA VALE DE ROSAS Nº 7^^PONTE DO ROL^^2560-150^||^^^^^^915369804|^^^^^^|||||383543824|||||||||||N\r"
				+ "PV1||Consultas|||||||||1||||||||5486248|||S\r"
				+ "ORC|SC|2060770|CCTV2020118074||CM||1.000\r"
				+ "OBR||2060770|CCTV2020118074|62009903^ECO PELVICA|||20201103144258|20201103160113||||||||||CCTV2020118074|||US|20201103040103||US|||1^^^20201103040103|||||16265|16265|16265&&José Rebelo||||||||||||CCTV2020118074^^ACCESSION_NUMBER\r"
				+ "OBX|1|PDF_BASE64|904476||JVBERi0xLjQKJeLjz9MKCONCATENATED_BASE_64_STRINGZgo2MTg0OQolJUVPRgo=||||||F\r";
		
		assertThrows(HL7Exception.class, () -> ContextSingleton.getInstance().getPipeParser().parse(messageString));
		
		String fixedString = MapperEngine.fixMessage(messageString);

		assertDoesNotThrow(() -> ContextSingleton.getInstance().getPipeParser().parse(fixedString));
	}

	@Test
	void testFixMessageWithNewlines() {
		String messageString = "MSH|^~\\&|CWM|CCB|PACS_CCB|CCB|20201130140403||ORU^R01|21c1dd72-7d42-4b89-b936-a4b1c0f59a26|P|2.4|||AL\r"
				+ "PID||JMS15392104|JMS15392104^^^JMS^NS|^^^N_BI|FONSECA^PAULO JORGE QUELHAS^||19700103|M|||RUA DO CRUZEIRO, Nº12 4  ESQ ^^LISBOA^^1300-164^||^^^^^^962441340|^^^^^^|||||386346510|||||||||||N\r"
				+ "PV1||Consultas|||||||||1||||||||5047372|||S\r"
				+ "ORC|SC|1392079|CCB2020027089||CM||1.000\r"
				+ "OBR||1392079|CCB2020027089|60010001^RX TORAX, PULMOES E CORACAO 1 INCIDENCIA|||20201127090315|20201130140403||||||||||CCB2020027089|||CR|20201130020350||CR|||1^^^20201130020350|||||23000|23000|23000&&Jorge Rodrigues||||||||||||CCB2020027089^^ACCESSION_NUMBER\r"
				+ "OBX|1|ED|945987||Radiografia do torax em PA\n"
				+ "Ligeira acentuacao da convexidade dos contornos cardiacos com ligeira acentuacao vascular hilar bilateral sem condensacao ou cavitacao do parenquima pulmonar. Seios pleurais permeaveis com elevacao da hemicupula diafragmatica direita.\n"
				+ "\n"
				+ "\n"
				+ "Dr(a): Jorge Rodrigues\n"
				+ "OM: 23000||||||F\r";

		assertThrows(HL7Exception.class, () -> ContextSingleton.getInstance().getPipeParser().parse(messageString));

		String fixedString = MapperEngine.fixMessage(messageString);

		assertDoesNotThrow(() -> ContextSingleton.getInstance().getPipeParser().parse(fixedString));
	}

	@Test
	void testAfterFieldEmpty() throws HL7Exception {
		String toBeReturned = "CUFC";
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS||20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|" + toBeReturned + "^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/MSH-6" };
		meng.fieldAfterOperation(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "/MSH-4", Category.AFTER_FIELD, errors);
		assertEquals(toBeReturned, terserSpy.get("/MSH-6"));
	}

	@Test
	void testFix() throws HL7Exception {
		String toBeReturned = "CUFC";
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS||20201117172651||ADT^A40|1604236349|P|2.4|||AL\r" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|" + toBeReturned + "^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000-ST|||||||||||||||1^PORTUGAL||N\r" +
		        "OBX||||||";

		MapperEngine meng = new MapperEngine();
		String fixedString = meng.fixMessage(messageString);
		assertDoesNotThrow(() -> ContextSingleton.getInstance().getPipeParser().parse(fixedString));

	}
}
