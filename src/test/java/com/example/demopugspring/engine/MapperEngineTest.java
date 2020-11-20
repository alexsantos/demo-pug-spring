package com.example.demopugspring.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r\n"+
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||198212090000|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";

		String toBeReturned = "19821209";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);
		
		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/PID-7", "/PID-13-7" };
		meng.replaceOperation(terserSpy, Arrays.asList(testeListaKeys), "(00)*", errors);
		assertEquals(toBeReturned, terserSpy.get("/PID-7"));
		assertEquals("9", terserSpy.get("/PID-13-7"));

	}

	@Test
	void testAddSNSOperation() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r\n"+
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";
		String toBeReturned = "1234512345";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);
		
		Mockito.doReturn(toBeReturned).when(terserSpy).get(Mockito.contains("/PID-19"));


		//outMessage.get(name, rep)
		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/PID-3" };
		meng.addFieldSNS(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "/PID-19", errors);
		assertEquals(toBeReturned, terserSpy.get("PID-3(2)"));
		assertEquals("SNS", terserSpy.get("PID-3(2)-4"));
	}

	@Test
	void testAddContact() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r\n" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^teste@teste.com^^^999999999|^^^^^^222222222||||||||||||||1^PORTUGAL||N\r\n";
		String toBeReturnedTelf = "999999999";
		String toBeReturnedDois = "222222222";
		String toBeReturnedEmail = "teste@teste.com";

		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);


		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/PID" };
		String field = testeListaKeys[0];
		meng.addContactRepetitions(terserSpy, field, terserSpy.get(field + "-13-7-1"), terserSpy.get(field + "-13-12-1"), terserSpy.get(field + "-14-7-1"), terserSpy.get(field + "-13-4-1"));
		assertEquals("X.400", terserSpy.get("PID-13(2)-3"));
		assertEquals(toBeReturnedEmail, terserSpy.get("PID-13(2)-4"));
		assertEquals(toBeReturnedTelf, terserSpy.get("PID-13(0)-12"));
		assertEquals(toBeReturnedDois, terserSpy.get("PID-13(1)-12"));


	}

	@Test
	void testClearIfOperation() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r\n" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";
		String toFilter = "CUFC";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);


		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/PID-3(#)-4" };
		meng.clearIfOperation(terserSpy, Arrays.asList(testeListaKeys), toFilter, errors);
		assertEquals(null, terserSpy.get("PID-3(1)-1"));
	}

	@Test
	void testClearIfOperationPatient() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r\n" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";
		String toFilter = "CUFC";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		outMessage.parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/PATIENT/PID-3(#)-4" };
		meng.clearIfOperation(terserSpy, Arrays.asList(testeListaKeys), toFilter, errors);
		assertEquals(null, terserSpy.get("/PATIENT/PID-3(1)-1"));
	}


	@Test
	void testAddSNSOperationPatient() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r\n" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";
		String toBeReturned = "1234512345";
		Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Terser t = new Terser(outMessage);
		Terser terserSpy = Mockito.spy(t);

		Mockito.doReturn(toBeReturned).when(terserSpy).get(Mockito.contains("/PATIENT/PID-19"));

		List<MapperError> errors = Mockito.mock(List.class);
		// outMessage.get(name, rep)
		MapperEngine meng = new MapperEngine();
		String[] testeListaKeys = { "/PATIENT/PID-3" };
		meng.addFieldSNS(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "/PATIENT/PID-19", errors);
		assertEquals(toBeReturned, terserSpy.get("/PATIENT/PID-3(2)"));
		assertEquals("SNS", terserSpy.get("/PATIENT/PID-3(2)-4"));
	}

	@Test
	void testTranscoding() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r\n" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";
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

		String[] testeListaKeys = { "/PID-28" };
		meng.countryCodes = codes;
		meng.transcode(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "GH-LOCATIONS", errors);
		assertEquals(toBeReturned, terserSpy.get("/PID-28-1"));
	}

	@Test
	void testTranscodingPatient() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r\n" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";
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

		String[] testeListaKeys = { "/PATIENT/PID-28" };
		meng.countryCodes = codes;
		meng.transcode(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "GH-LOCATIONS", errors);
		assertEquals(toBeReturned, terserSpy.get("/PATIENT/PID-28-1"));
	}

	@Test
	void testTranscodingRepetion() throws HL7Exception {
		String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A31|1604236349|P|2.4|||AL\r\n" +
				"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";
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

		String[] testeListaKeys = { "/PID-3(#)-4" };
		meng.identificationCodes = codes;
		meng.transcode(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "IDENTIFICATIONS", errors);
		assertEquals(toBeReturned, terserSpy.get("/PID-3(0)-4"));
		assertEquals(toBeReturned2, terserSpy.get("/PID-3(1)-4"));

	}


		@Test
		void testJoinFieldsPatient() throws HL7Exception {
			String toBeReturned = "995896186";
			String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|CUFC|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r\n" +
					"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|995896186^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";

			Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
			Terser t = new Terser(outMessage);
			Terser terserSpy = Mockito.spy(t);

			MapperEngine meng = new MapperEngine();
			String[] testeListaKeys = { "/PATIENT/PID-3" };
			meng.joinFields(terserSpy, Arrays.asList(testeListaKeys), "/PATIENT/PID-4", errors);
			assertEquals(toBeReturned, terserSpy.get("/PATIENT/PID-3(2)"));
			assertEquals("NIF", terserSpy.get("/PATIENT/PID-3(2)-4"));

		}

		@Test
		void testField() throws HL7Exception {
			String toBeReturned = "CUFC";
			String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|TESTE|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r\n" +
					"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|" + toBeReturned + "^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";

			Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
			Terser t = new Terser(outMessage);
			Terser terserSpy = Mockito.spy(t);

			MapperEngine meng = new MapperEngine();
			String[] testeListaKeys = { "/MSH-6" };
			meng.mapper(terserSpy, terserSpy, Arrays.asList(testeListaKeys), "/MSH-4", Category.FIELD, errors);
			assertEquals(toBeReturned, terserSpy.get("/MSH-6"));

		}

		@Test
		void testText() throws HL7Exception {
			String toBeReturned = "INTEGRACAO";
			String messageString = "MSH|^~\\&|GH|CUFC|ehCOS|TESTE|20201117172651||ADT^A40|1604236349|P|2.4|||AL\r\n" +
					"PID|||42341818^^^JMS^NS~684028^^^CUFC^NS|" + toBeReturned + "^^^NIF^PT|SEGUNDO^CLIENTE TESTE ECOS CUFC O||19821209|M|||^^^^^1||^^^^^^900000000|||||||||||||||1^PORTUGAL||N\r\n";

			Message outMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
			Terser t = new Terser(outMessage);
			Terser terserSpy = Mockito.spy(t);

			MapperEngine meng = new MapperEngine();
			String[] testeListaKeys = { "/MSH-6" };
			meng.mapper(terserSpy, terserSpy, Arrays.asList(testeListaKeys), toBeReturned, Category.TEXT, errors);
			assertEquals(toBeReturned, terserSpy.get("/MSH-6"));

		}


}
