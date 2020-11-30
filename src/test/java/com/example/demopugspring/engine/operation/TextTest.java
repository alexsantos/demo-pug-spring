package com.example.demopugspring.engine.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.factory.ContextSingleton;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.util.Terser;

class TextTest {

	@Test
	void testMapField() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r"
				+ "ORC|NW||CCTV2020118074||||1.000\r"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		List<String> keys = Arrays.asList("/MSH-4");

		String value = "NEW_VALUE";

		AbstractOperation operation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		assertEquals("CCTV", outgoingTerser.get("/MSH-4"));
		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("NEW_VALUE", outgoingTerser.get("/MSH-4"));
	}

	@Test
	void testMapFieldRepetitions() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r"
				+ "ORC|NW||CCTV2020118074||||1.000\r"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		List<String> keys = Arrays.asList("/PATIENT/PID-4(#)");

		String value = "1234";

		AbstractOperation operation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type[] pid4Fields = outgoingTerser.getSegment("/PATIENT/PID").getField(4);
		assertEquals("244288437^^^NIF^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI", pid4Fields[2].encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("1234^^^NIF^PT", pid4Fields[0].encode());
		assertEquals("1234^^^N_BENEF", pid4Fields[1].encode());
		assertEquals("1234^^^N_BI", pid4Fields[2].encode());
	}

	@Test
	void testMapComponent() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r"
				+ "ORC|NW||CCTV2020118074||||1.000\r"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		List<String> keys = Arrays.asList("/PATIENT/PID-4-1");

		String value = "NEW_VALUE";

		AbstractOperation operation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		assertEquals("244288437", outgoingTerser.get("/PATIENT/PID-4-1"));

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("NEW_VALUE", outgoingTerser.get("/PATIENT/PID-4-1"));
	}

	@Test
	void testMapComponentRepetitions() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r"
				+ "ORC|NW||CCTV2020118074||||1.000\r"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		List<String> keys = Arrays.asList("/PATIENT/PID-4(#)-4");

		String value = "NEW_VALUE";

		AbstractOperation operation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type[] pid4Fields = outgoingTerser.getSegment("/PATIENT/PID").getField(4);
		assertEquals("244288437^^^NIF^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI", pid4Fields[2].encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("244288437^^^NEW_VALUE^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^NEW_VALUE", pid4Fields[1].encode());
		assertEquals("12586669^^^NEW_VALUE", pid4Fields[2].encode());
	}

	@Test
	void testMapAllComponents() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r"
				+ "ORC|NW||CCTV2020118074||||1.000\r"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		List<String> keys = Arrays.asList("/PATIENT/PID-4-#");

		String value = "1234";

		AbstractOperation operation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type pid4Field = outgoingTerser.getSegment("/PATIENT/PID").getField(4, 0);
		assertEquals("244288437^^^NIF^PT", pid4Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("1234^1234^1234^1234^1234^1234^1234^1234", pid4Field.encode());
	}

	@Test
	void testMapSubcomponent() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r"
				+ "ORC|NW||CCTV2020118074||||1.000\r"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		List<String> keys = Arrays.asList("/ORDER/ORDER_DETAIL/OBR-32-1-1");

		String value = "NEW_VALUE";

		AbstractOperation operation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type obr32Field = outgoingTerser.getSegment("/ORDER/ORDER_DETAIL/OBR").getField(32, 0);

		assertEquals("123440432&REBELO&JOSE", obr32Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("NEW_VALUE&REBELO&JOSE", obr32Field.encode());
	}

	@Test
	void testMapSubcomponentRepetitions() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF&1^PT~11931489544^^^N_BENEF&2~12586669^^^N_BI&3|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r"
				+ "ORC|NW||CCTV2020118074||||1.000\r"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		List<String> keys = Arrays.asList("/PATIENT/PID-4(#)-4-2");

		String value = "1234";

		AbstractOperation operation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type[] pid4Fields = outgoingTerser.getSegment("/PATIENT/PID").getField(4);

		assertEquals("244288437^^^NIF&1^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF&2", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI&3", pid4Fields[2].encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("244288437^^^NIF&1234^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF&1234", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI&1234", pid4Fields[2].encode());
	}

	@Test
	void testMapAllSubcomponents() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r"
				+ "ORC|NW||CCTV2020118074||||1.000\r"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		List<String> keys = Arrays.asList("/ORDER/ORDER_DETAIL/OBR-32-1-#");

		String value = "NEW_VALUE";

		AbstractOperation operation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type obr32Field = outgoingTerser.getSegment("/ORDER/ORDER_DETAIL/OBR").getField(32, 0);

		assertEquals("123440432&REBELO&JOSE", obr32Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE", obr32Field.encode());
	}
}
