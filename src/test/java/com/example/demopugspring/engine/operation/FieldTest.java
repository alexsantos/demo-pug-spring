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

class FieldTest {

	@Test
	void testMapFieldMsg() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PATIENT_VISIT/PV1-2";
		String key2 = "/PATIENT/PATIENT_VISIT/PV1-3";
		List<String> keys = Arrays.asList(new String[] { key1, key2 });

		// Change /PATIENT/PATIENT_VISIT/PV1-14 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/PATIENT/PATIENT_VISIT/PV1-14"), "NEW_VALUE");
		textOperation.map();

		String value = "/[MSG]/PATIENT/PATIENT_VISIT/PV1-14";

		AbstractOperation fieldOperation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		assertEquals("Consultas", outgoingTerser.get(key1));
		assertEquals("3", outgoingTerser.get(key2));

		fieldOperation.map();

		assertTrue(fieldOperation.errors.isEmpty());
		assertEquals("CONSULTAS", outgoingTerser.get(key1));
		assertEquals("CONSULTAS", outgoingTerser.get(key2));
	}

	@Test
	void testMapFieldTmp() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PATIENT_VISIT/PV1-2";
		String key2 = "/PATIENT/PATIENT_VISIT/PV1-3";
		List<String> keys = Arrays.asList(new String[] { key1, key2 });

		// Change /PATIENT/PATIENT_VISIT/PV1-14 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/PATIENT/PATIENT_VISIT/PV1-14"), "NEW_VALUE");
		textOperation.map();

		String value = "/[TMP]/PATIENT/PATIENT_VISIT/PV1-14";

		AbstractOperation fieldOperation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		assertEquals("Consultas", outgoingTerser.get(key1));
		assertEquals("3", outgoingTerser.get(key2));

		fieldOperation.map();

		assertTrue(fieldOperation.errors.isEmpty());
		assertEquals("NEW_VALUE", outgoingTerser.get(key1));
		assertEquals("NEW_VALUE", outgoingTerser.get(key2));
	}

	@Test
	void testMapFieldRepetitionsMsg() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4(#)";
		List<String> keys = Arrays.asList(new String[] { key1 });

		// Change /PATIENT/PATIENT_VISIT/PV1-3 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/PATIENT/PATIENT_VISIT/PV1-3"), "404");
		textOperation.map();

		String value = "/[MSG]/PATIENT/PATIENT_VISIT/PV1-3";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type[] pid4Fields = outgoingTerser.getSegment("/PATIENT/PID").getField(4);
		assertEquals("244288437^^^NIF^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI", pid4Fields[2].encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("3^^^NIF^PT", pid4Fields[0].encode());
		assertEquals("3^^^N_BENEF", pid4Fields[1].encode());
		assertEquals("3^^^N_BI", pid4Fields[2].encode());
	}

	@Test
	void testMapFieldRepetitionsTmp() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4(#)";
		List<String> keys = Arrays.asList(new String[] { key1 });

		// Change /PATIENT/PATIENT_VISIT/PV1-3 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/PATIENT/PATIENT_VISIT/PV1-3"), "404");
		textOperation.map();

		String value = "/[TMP]/PATIENT/PATIENT_VISIT/PV1-3";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type[] pid4Fields = outgoingTerser.getSegment("/PATIENT/PID").getField(4);
		assertEquals("244288437^^^NIF^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI", pid4Fields[2].encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("404^^^NIF^PT", pid4Fields[0].encode());
		assertEquals("404^^^N_BENEF", pid4Fields[1].encode());
		assertEquals("404^^^N_BI", pid4Fields[2].encode());
	}

	@Test
	void testMapComponentMsg() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4-1";
		String key2 = "/ORDER/ORDER_DETAIL/OBR-4-2";
		List<String> keys = Arrays.asList(new String[] { key1, key2 });

		// Change /PATIENT/PID-3-1 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/PATIENT/PID-3-1"), "NEW_VALUE");
		textOperation.map();

		String value = "/[MSG]/PATIENT/PID-3-1";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		assertEquals("244288437", outgoingTerser.get(key1));
		assertEquals("ECO PELVICA", outgoingTerser.get(key2));

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("JMS43417401", outgoingTerser.get(key1));
		assertEquals("JMS43417401", outgoingTerser.get(key2));
	}

	@Test
	void testMapComponentTmp() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4-1";
		String key2 = "/ORDER/ORDER_DETAIL/OBR-4-2";
		List<String> keys = Arrays.asList(new String[] { key1, key2 });

		// Change /PATIENT/PID-3-1 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/PATIENT/PID-3-1"), "NEW_VALUE");
		textOperation.map();

		String value = "/[TMP]/PATIENT/PID-3-1";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		assertEquals("244288437", outgoingTerser.get(key1));
		assertEquals("ECO PELVICA", outgoingTerser.get(key2));

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("NEW_VALUE", outgoingTerser.get(key1));
		assertEquals("NEW_VALUE", outgoingTerser.get(key2));
	}

	@Test
	void testMapComponentRepetitionsMsg() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4(#)-4";
		List<String> keys = Arrays.asList(new String[] { key1 });

		// Change /PATIENT/PID-3-1 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/PATIENT/PID-3-1"), "NEW_VALUE");
		textOperation.map();

		String value = "/[MSG]/PATIENT/PID-3-1";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type[] pid4Fields = outgoingTerser.getSegment("/PATIENT/PID").getField(4);
		assertEquals("244288437^^^NIF^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI", pid4Fields[2].encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("244288437^^^JMS43417401^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^JMS43417401", pid4Fields[1].encode());
		assertEquals("12586669^^^JMS43417401", pid4Fields[2].encode());
	}

	@Test
	void testMapComponentRepetitionsTmp() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4(#)-4";
		List<String> keys = Arrays.asList(new String[] { key1 });

		// Change /PATIENT/PID-3-1 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/PATIENT/PID-3-1"), "NEW_VALUE");
		textOperation.map();

		String value = "/[TMP]/PATIENT/PID-3-1";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

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
	void testMapAllComponentsMsg() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||20201108^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4-#";
		String key2 = "/ORDER/ORDER_DETAIL/OBR-4-#";
		List<String> keys = Arrays.asList(new String[] { key1, key2 });

		// Change /ORDER/ORDER_DETAIL/OBR-46-1 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/ORDER/ORDER_DETAIL/OBR-46-1"), "1234");
		textOperation.map();

		String value = "/[MSG]/ORDER/ORDER_DETAIL/OBR-46-1";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type pid4Field = outgoingTerser.getSegment("/PATIENT/PID").getField(4, 0);
		assertEquals("244288437^^^NIF^PT", pid4Field.encode());

		Type obr32Field = outgoingTerser.getSegment("/ORDER/ORDER_DETAIL/OBR").getField(4, 0);
		assertEquals("62009903^ECO PELVICA", obr32Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("20201108^20201108^20201108^20201108^20201108^20201108^20201108^20201108", pid4Field.encode());
		assertEquals("20201108^20201108^20201108^20201108^20201108^20201108", obr32Field.encode());
	}

	@Test
	void testMapAllComponentsTmp() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||20201108^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4-#";
		String key2 = "/ORDER/ORDER_DETAIL/OBR-4-#";
		List<String> keys = Arrays.asList(new String[] { key1, key2 });

		// Change /ORDER/ORDER_DETAIL/OBR-46-1 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/ORDER/ORDER_DETAIL/OBR-46-1"), "1234");
		textOperation.map();

		String value = "/[TMP]/ORDER/ORDER_DETAIL/OBR-46-1";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type pid4Field = outgoingTerser.getSegment("/PATIENT/PID").getField(4, 0);
		assertEquals("244288437^^^NIF^PT", pid4Field.encode());

		Type obr32Field = outgoingTerser.getSegment("/ORDER/ORDER_DETAIL/OBR").getField(4, 0);
		assertEquals("62009903^ECO PELVICA", obr32Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("1234^1234^1234^1234^1234^1234^1234^1234", pid4Field.encode());
		assertEquals("1234^1234^1234^1234^1234^1234", obr32Field.encode());
	}

	@Test
	void testMapSubcomponentMsg() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/ORDER/ORDER_DETAIL/OBR-32-1-1";
		String key2 = "/ORDER/ORDER_DETAIL/OBR-32-1-3";
		List<String> keys = Arrays.asList(new String[] { key1, key2 });

		// Change /ORDER/ORDER_DETAIL/OBR-32-1-2 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/ORDER/ORDER_DETAIL/OBR-32-1-2"), "NEW_VALUE");
		textOperation.map();

		String value = "/[MSG]/ORDER/ORDER_DETAIL/OBR-32-1-2";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type obr32Field = outgoingTerser.getSegment("/ORDER/ORDER_DETAIL/OBR").getField(32, 0);

		assertEquals("123440432&NEW_VALUE&JOSE", obr32Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("REBELO&NEW_VALUE&REBELO", obr32Field.encode());
	}

	@Test
	void testMapSubcomponentTmp() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/ORDER/ORDER_DETAIL/OBR-32-1-1";
		String key2 = "/ORDER/ORDER_DETAIL/OBR-32-1-3";
		List<String> keys = Arrays.asList(new String[] { key1, key2 });

		// Change /ORDER/ORDER_DETAIL/OBR-32-1-2 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/ORDER/ORDER_DETAIL/OBR-32-1-2"), "NEW_VALUE");
		textOperation.map();

		String value = "/[TMP]/ORDER/ORDER_DETAIL/OBR-32-1-2";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type obr32Field = outgoingTerser.getSegment("/ORDER/ORDER_DETAIL/OBR").getField(32, 0);

		assertEquals("123440432&NEW_VALUE&JOSE", obr32Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("NEW_VALUE&NEW_VALUE&NEW_VALUE", obr32Field.encode());
	}

	@Test
	void testMapSubcomponentRepetitionsMsg() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF&1^PT~11931489544^^^N_BENEF&2~12586669^^^N_BI&3|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4(#)-4-2";
		List<String> keys = Arrays.asList(new String[] { key1 });

		// Change /ORDER/ORDER_DETAIL/OBR-32-1-2 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/ORDER/ORDER_DETAIL/OBR-32-1-2"), "NEW_VALUE");
		textOperation.map();

		String value = "/[MSG]/ORDER/ORDER_DETAIL/OBR-32-1-2";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type[] pid4Fields = outgoingTerser.getSegment("/PATIENT/PID").getField(4);

		assertEquals("244288437^^^NIF&1^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF&2", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI&3", pid4Fields[2].encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("244288437^^^NIF&REBELO^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF&REBELO", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI&REBELO", pid4Fields[2].encode());
	}

	@Test
	void testMapSubcomponentRepetitionsTmp() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF&1^PT~11931489544^^^N_BENEF&2~12586669^^^N_BI&3|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/PATIENT/PID-4(#)-4-2";
		List<String> keys = Arrays.asList(new String[] { key1 });

		// Change /ORDER/ORDER_DETAIL/OBR-32-1-2 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/ORDER/ORDER_DETAIL/OBR-32-1-2"), "NEW_VALUE");
		textOperation.map();

		String value = "/[TMP]/ORDER/ORDER_DETAIL/OBR-32-1-2";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type[] pid4Fields = outgoingTerser.getSegment("/PATIENT/PID").getField(4);

		assertEquals("244288437^^^NIF&1^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF&2", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI&3", pid4Fields[2].encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("244288437^^^NIF&NEW_VALUE^PT", pid4Fields[0].encode());
		assertEquals("11931489544^^^N_BENEF&NEW_VALUE", pid4Fields[1].encode());
		assertEquals("12586669^^^N_BI&NEW_VALUE", pid4Fields[2].encode());
	}

	@Test
	void testMapAllSubcomponentsMsg() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/ORDER/ORDER_DETAIL/OBR-32-1-#";
		List<String> keys = Arrays.asList(new String[] { key1 });

		// Change /ORDER/ORDER_DETAIL/OBR-32-1-2 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/ORDER/ORDER_DETAIL/OBR-32-1-2"), "NEW_VALUE");
		textOperation.map();

		String value = "/[MSG]/ORDER/ORDER_DETAIL/OBR-32-1-2";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type obr32Field = outgoingTerser.getSegment("/ORDER/ORDER_DETAIL/OBR").getField(32, 0);

		assertEquals("123440432&NEW_VALUE&JOSE", obr32Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("REBELO&REBELO&REBELO&REBELO&REBELO&REBELO&REBELO&REBELO&REBELO&REBELO&REBELO", obr32Field.encode());
	}

	@Test
	void testMapAllSubcomponentsTmp() throws HL7Exception {
		String messageString = "MSH|^~\\&|MCDTS|CCTV|PACS_CCTV|CCTV|20201103124625||ORM^O01|20263428|P|2.4|||AL\r\n"
				+ "PID|||JMS43417401^^^JMS^NS|244288437^^^NIF^PT~11931489544^^^N_BENEF~12586669^^^N_BI|CUNHA^LETICIA^SOFIA||19830224000000|F|||RUA VALE DE ROSAS No 7^^PONTE DO ROL^^2560-150^1||^^^LETICIA.SABARIZ@HOTMAIL.COM^^^915369804||||||383543824||||;;;|||||||N\r\n"
				+ "PV1||Consultas|3||||||||1|||CONSULTAS|||||5486248|||S\r\n"
				+ "ORC|NW||CCTV2020118074||||1.000\r\n"
				+ "OBR|||CCTV2020118074|62009903^ECO PELVICA|||20201103124613|20201103124613|||||||||||||US_CCTV|||US_CCTV|||1^^^20201103124613|||||123440432&REBELO&JOSE||||||||||||||2020118074^^ACCESSION_NUMBER\r\n"
				+ "NTE|||<BR/>OBSERVACOES MARCACAO - CONFIRMADO VIA SMS DE CLIENTE|^OBS^OBS\r\n"
				+ "NTE|||37 ANOS?ROTINA?1a MAMOGRAFIA DE RASTREIO|^OBS_PEDIDO_EXT^OBS_PEDIDO_EXT";

		MapperEngine meng = new MapperEngine();
		Message incomingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);
		Message outgoingMessage = ContextSingleton.getInstance().getPipeParser().parse(messageString);

		Terser incomingTerser = new Terser(incomingMessage);
		Terser outgoingTerser = new Terser(outgoingMessage);

		String key1 = "/ORDER/ORDER_DETAIL/OBR-32-1-#";
		List<String> keys = Arrays.asList(new String[] { key1 });

		// Change /ORDER/ORDER_DETAIL/OBR-32-1-2 value first
		AbstractOperation textOperation = new Text(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, Arrays.asList("/ORDER/ORDER_DETAIL/OBR-32-1-2"), "NEW_VALUE");
		textOperation.map();

		String value = "/[TMP]/ORDER/ORDER_DETAIL/OBR-32-1-2";

		AbstractOperation operation = new Field(meng, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);

		Type obr32Field = outgoingTerser.getSegment("/ORDER/ORDER_DETAIL/OBR").getField(32, 0);

		assertEquals("123440432&NEW_VALUE&JOSE", obr32Field.encode());

		operation.map();

		assertTrue(operation.errors.isEmpty());
		assertEquals("NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE&NEW_VALUE", obr32Field.encode());
	}
}
