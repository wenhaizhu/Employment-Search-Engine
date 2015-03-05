
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PageRank {
	private int fileSize = 11212;
	private List<HashSet<Integer>> graph = new LinkedList<HashSet<Integer>>();

	public PageRank() {

		// TODO Auto-generated constructor stub
		for (int i = 0; i < this.fileSize; i++) {
			graph.add(new HashSet<Integer>());
		}
	}

	public Map<String, List<Integer>> buildHashMap() {
		// this function is used to build the hashmap to contain the attribute values
		// and the files contain it. 
		Map<String, List<Integer>> hashMap = new WeakHashMap<String, List<Integer>>();
		String fileFolder = "/home/winhigh/Downloads/data/";
		File document = new File(fileFolder);
		JSONParser parser = new JSONParser();
		System.out.println("Building hashMap");
		try {
			for (int i = 0; i < fileSize; i++) {

				String name = fileFolder + i + ".json";
				File file = new File(name);
				FileReader fr = new FileReader(file);
				JSONObject obj = (JSONObject) parser.parse(fr);
				String idS = (String) obj.get("id");
				int id = Integer.parseInt((String) idS);
				String company = (String) obj.get("company");
				String title = (String) obj.get("title")
						+ (String) obj.get("location");

				String geo = (String) obj.get("location")
						+ (String) obj.get("jobtype");

				if (!hashMap.containsKey(company)) {
					hashMap.put(company, new ArrayList<Integer>());
				}
				hashMap.get(company).add(id);
				if (!hashMap.containsKey(title)) {
					hashMap.put(title, new ArrayList<Integer>());
				}
				hashMap.get(title).add(id);

				if (!hashMap.containsKey(geo)) {
					hashMap.put(geo, new ArrayList<Integer>());

				}
				hashMap.get(geo).add(id);

				if ((i+ 1) % 100 == 0) {
					buildGraph(hashMap);
					hashMap.clear();
					System.gc();

				}
				if((i+1)>(fileSize/100)*100)
				{
					buildGraph(hashMap);
				
				}

				fr.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Building hashMap finished");
		System.gc();
		return hashMap;

	}

	public void buildGraph(Map<String, List<Integer>> hashMap) {
		// this function is to build the graph. the files are nodes and if two files
		// have relations, we will connect them
		System.out.println("start");
		Iterator it = hashMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			List<Integer> value = (ArrayList<Integer>) entry.getValue();
			for (int i : value) {
				graph.get(i).addAll(value);
			}
		}
		System.out.println("end");
		System.gc();
		// return graph;
	}

	public List<Double> computePageRank(List<HashSet<Integer>> graph) {
		// this function is to compute the rank of each file
		System.out.println("start2");
		List<Double> pageRank = new ArrayList<Double>();
		for (int i = 0; i < graph.size(); i++) {
			pageRank.add(1.0);
		}
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < graph.size(); j++) {
				double sum = 0;
				for (int v : graph.get(j)) {
					if (v == j)
						continue;
					sum += pageRank.get(v) / (graph.get(v).size() - 1);
				}
				pageRank.set(j, 0.15 + 0.85 * sum);
			}
			System.out.println(i);
		}
		System.out.println("end2");
		System.gc();
		return pageRank;
	}

	public HashMap<String, String> buildCatMap() {
		// this function is to build the area - keyword hashmap
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String[] keywords = {
				"vendedor,cocinero,vendedores, cajera, camarera, vendedora,almacenista,promotoras, cajeros, sueldos, esteticista, sushiman, chef, bartender, estilista, cheff,servicio, venta, dietas,ventas,cocina, recepcionista，  compras, promotor, visitador, almacenistas, inventarios, cafetero, publicitario, medios, sales, comercio, restaurante, salon, pastelera, teleoperadores, franquicias, repartidor",
				"ingeniero, civil, sistemas, tecnico, electronico, impulsadoras,  grafico, electrome, data, entry, electromecanico, soporte , programador, archivo, calidad, digitador,  php, project, taller, obra,  java, .net, tester, os, front end, técnico, qa, cargue, diseñador, mecánicos, mecanico, soldador, it ， web, ios, obrero, operarios, mecanicos, industrial, desarrollador, control, controller, montacarga, produccion, developer, arquitecto, delineante, software, planta, producto, proveedores, pasantia, capataz, coordinador, electricista, empleado, arquitectura, facturador, impulsadores, obras, programadores, responsable, laboratorio, linux, encargada",
				"personal, limpieza, comprador, mucama,  mayordomo, domiciliario, manicuristas, zona, seguridad, logistico, carniceros, cobranzas, aseadora, bolivar, bordadora, domiciliarios, modista, villa, vigilancia, costureras, bodega",
				"comercial, contable, auxiliar, secretaria, comerciales, administrativa, funcional, asesores, ejecutivo, cajero, telemercaderista, publico, , cartera, supervisor, recursos, , abogado, manager, aprendices,financiero,despacho,consultor,marketing, procuradora, impositivo  ,tesoreria,contabilidad,liquidador,cliente,agentes,analista,nomina,humano,proyectos,encargado,secretarias,asesor,,exterior,contador,necesita,administrativo,agente,asesoras,operativo,asistente,humana,mercadeo,,gerente,auditores,official,operaria,auditor,civiles,documentador,cuentas",
				"enfermeria, medico, nutricionista, farmacia, fonoaudiologa, terapeuta, fisioterapeuta, dental, odontologo, odontología,extraccionista,quirúrgica,,integradora, médicos,terapéutico,quirúrgico,psicologia,practicante,psicologo,veterinario, salud, psicomotricista,enfermera,enfermeros,pizzero,ambulancia,fisioterapeutas,fonoaudiologo,kinesiologos,medicos,dicos",
				"docentes, profesora，bachilleres,  geologo, profesor,teacher, metrologoestudiante, pintores, bacheros, economista, dibujante, derecho, hornero, docente, estudiantes, instrumentista",
				"conductor, ayudante, operario, servicios, empleada, cadete, mensajero, mesero, tornero, mantenimiento, parrillero, guarda, panadero, barman, teward, , portero, ebanista, foguista，albañil,cosmetologa，cerrajero, prensista, logistica, ejecutivos,inspector,lavacopas,,social,servidores, escolta, calderista, chofer, colorista, instaladores, para, delivery, instalador, carpinteria, balancineros, caveros, ejero, empacador, domestica, deposito" };
		String[] categories = { "commerical", "industrial", "residential",
				"business", "medical", "education", "service" };

		for (int i = 0; i < categories.length; i++) {
			String[] strings = keywords[i].split(",");
			for (String s : strings) {
				hashMap.put(s.toLowerCase().trim(), categories[i]);
			}
		}
		return hashMap;
	}

	public String findCategory(String title, HashMap<String, String> hashMap) {
		// this function is to find the area of a certain job title
		String[] sub = title.split("\\s");
		for (String s : sub) {
			s = s.toLowerCase().trim();
			if (hashMap.containsKey(s)) {
				return hashMap.get(s);
			}
		}
		return "service";
	}

	public int caculateDuration(String str1, String str2) {
		// this is to compute the difference between two dates
		String pattern = "\\d{4}-\\d+-\\d+";
		Pattern r = Pattern.compile(pattern);
		if (str1 == null || str2 == null || str1.equals("") || str2.equals("")) {
			// System.out.print("1");
			return -1;
		} else {
			Matcher m = r.matcher(str1);
			Matcher n = r.matcher(str2);
			if (!m.find() || !n.find()) {
				// System.out.print("2888");
				return -1;
			} else {
				String[] date1 = str1.split("-");
				String[] date2 = str2.split("-");
				int diff = 0;
				int diffYear = Integer.valueOf(date2[0])
						- Integer.valueOf(date1[0]);
				int diffMonth = Integer.valueOf(date2[1])
						- Integer.valueOf(date1[1]);
				int diffDay = Integer.valueOf(date2[2])
						- Integer.valueOf(date1[2]);
				diff = diffYear * 365 + diffMonth * 30 + diffDay;
				return diff;
			}
		}

	}

	public void writeIntoFile(List<Double> pageRank) {
		// this is write the fields into json files
		String fileFolder = "/home/winhigh/Downloads/data/";
		JSONParser parser = new JSONParser();
		System.out.println("writing into file");
		HashMap<String, String> catMap = buildCatMap();
		try {
			for (int i = 0; i <fileSize; i++) {

				String name = fileFolder + i + ".json";
				// System.out.println(i);
				File file = new File(name);
				FileReader fr = new FileReader(file);
				JSONObject obj = (JSONObject) parser.parse(fr);
				obj.put("pagerank", pageRank.get(i));
				String title = (String) obj.get("title");
				String category = findCategory(title, catMap);
				obj.put("category", category);
				String firstSeen = (String) obj.get("firstSeenData");
				String lastSeen = (String) obj.get("lastSeenData");
				// System.out.println(firstSeen + " " + lastSeen);
				int diff = caculateDuration(firstSeen, lastSeen);
				obj.put("interval", diff);
				FileWriter filew = new FileWriter(name);
				filew.write(obj.toJSONString());
				filew.flush();
				filew.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Building hashMap finished");
		System.gc();
	}

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		PageRank pr = new PageRank();

		Map<String, List<Integer>> hashMap = pr.buildHashMap();
		// List<HashSet<Integer>> graph = pr.buildGraph(hashMap);
		hashMap.clear();
		List<Double> pageRank = pr.computePageRank(pr.graph);
		// File fileNew = new
		// File("/Users/zhaodongni/Documents/572/assignment2/result.txt");
		// BufferedWriter out = new BufferedWriter(new FileWriter(fileNew));

		for (int i = 0; i < pageRank.size(); i++) {
			System.out.println(i + ": " + pageRank.get(i));
			// out.write(""+pageRank.get(i)+"\n");
		}
		// out.close();
		pr.writeIntoFile(pageRank);
		long endTime = System.currentTimeMillis();
		System.out.print(endTime - startTime);
	}
}
