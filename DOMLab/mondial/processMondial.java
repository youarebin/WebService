package mondial;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.io.*;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class processMondial {
	static final String inputFile = "mondial/mondial.xml";  // 나중에 "mondial/mondial.xml"로 변경해서 테스트 
	static final String outputFile = "mondial/result.xml";
	
	// 대륙별 인구 계산 및 출력을 위해 대륙 이름들을 저장한 배열 정의
    static final String continent[] = new String[] { "Africa", "America", "Asia", "Australia", "Europe" };
    // 각 대륙의 인구 값들을 저장할 배열 선언 (순서는 위 배열의 이름들과 일치)
    static long pop[] = new long[5];
 
	public static void main(String[] args) throws Exception {
		// DOM 파서 생성
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		// XML 문서 파싱하기
		Document document = builder.parse(inputFile);

		Element mondial = document.getDocumentElement();
		
		// 대륙별 인구를 계산 및 출력 (3번)
		computePopulationsOfContinents(mondial);					
		
		// 종교별 신자 수를 계산 및 출력 (4번)
		// computeBelieversOfReligions(mondial);	
				
		// 국가별 정보 검색 및 출력 (1번)
		processCountries1(mondial);	
		
		// 국가별 정보 검색 및 DOM 트리 수정 (2번)
		processCountries2(mondial);	
		
		// 처리 결과 출력을 위한 변환기 생성
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();

		// 출력 포맷 설정: XML 선언과 문서 유형 선언 내용 설정하기
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		// transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
		// "mondial.dtd");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		// DOMSource 객체 생성
		DOMSource source = new DOMSource(document);

		// StreamResult 객체 생성
		StreamResult result = new StreamResult(new File(outputFile));

		// 파일로 저장하기
		transformer.transform(source, result);
		
		System.out.println();
		System.out.println(outputFile + "로 저장되었습니다.");
	}

	public static void processCountries1(Element mondial) {//#1
		NumberFormat formatter = NumberFormat.getInstance();
		
		for(Node ch = mondial.getFirstChild(); ch != null ; ch = ch.getNextSibling()) {
			if(ch.getNodeName().equals("country")) {//<country>
				Element country = (Element)ch;//Element타입 변수로 참조
				//1-1 국가 이름
				Node name = country.getFirstChild();//<name>Albania</name>
				Text txt = (Text)name.getFirstChild();
				String countryName = txt.getData();
				// 또는 = name.getFirstChild().getNodeValue();
                // 또는 = name.getTextContent();
				System.out.println(countryName);
				
				//1-2 국가 면적
				Node area = country.getAttributes().getNamedItem("area");
				String areaValue = area.getNodeValue();
				System.out.println("-면적: " + formatter.format(Double.parseDouble(areaValue)));
			
				//1-3 인구
				Node popNode = name.getNextSibling();
				if(popNode.getNodeName().equals("population")) {
					System.out.println("- 인구: " + 
                            formatter.format(Double.parseDouble(popNode.getTextContent())));
				}
				else {
					 System.out.println("- 인구 정보 없음");
				}
				
				//1-4 수도의 이름
				Element capital = null;
				String capitalId = country.getAttribute("capital");
				if(!capitalId.isEmpty()) {
					capital = country.getOwnerDocument().getElementById(capitalId);
					String capitalName = capital.getFirstChild().getTextContent();
					System.out.println("- 수도: " + capitalName);
				}
			System.out.println();
			}
		}
		
	} 
	
	public static void processCountries2(Element mondial) {
		NodeList list = mondial.getElementsByTagName("country");
        for (int i = 0; i < list.getLength(); i++) {
        	Node ch = list.item(i);
            Element country = (Element)ch;//미리 Element타입 변수로 참조
            
            //<area>생성해서 <name>다음에 추가
            String areaValue = country.getAttribute("area");//"28750"
            Document doc = country.getOwnerDocument();
            Element areaElm = doc.createElement("area");//<area></area>
            Text areaTxt = doc.createTextNode(areaValue);
            areaElm.appendChild(areaTxt);//<area>28750</area>
            Node name = country.getFirstChild();//<name>Albania</name>
            Node population = name.getNextSibling();
            country.insertBefore(areaElm, population);
            
            // 2-2 수도에 해당하는 <city>(및 그 subtree)를 DOM 트리에서 찾아 미리 제거해 둠
            Element capital = null;
            String capitalId = country.getAttribute("capital");
            if(!capitalId.isEmpty()) {
            	capital = country.getOwnerDocument().getElementById(capitalId);
            	Node parent = capital.getParentNode();
            	parent.removeChild(capital);
            }
            
            // 2-3 <population> 이후의 형제 노드들을 모두 삭제
            Node child = population.getNextSibling();
            while(child != null) {
            	Node nextChild = child.getNextSibling();
            	country.removeChild(child);
            	child = nextChild;
            }
            /*
             * // 또는
             * for (Node child = population.getNextSibling(); child != null; ) { 
             *     Node nextChild = child.getNextSibling(); 
             *     country.removeChild(child); 
             *     child = nextChild; 
             * }
             */
            
            // 2-4 수도에 해당하는 <city>(및 그 subtree)를 <country>의 마지막 자식으로 추가
            if(capital != null) {
            	country.appendChild(capital);
            	// <city>의 id를 제외한 모든 속성들을 삭제 
            	String id = capital.getAttribute("id");
            	NamedNodeMap attrs = capital.getAttributes();
            	while(attrs.getLength() > 0) {//모든 속성 삭제
            		Attr attrNode = (Attr)attrs.item(0);
            		capital.removeAttributeNode(attrNode);
            		// 또는 country.removeAttribute(attrNode.getName());
                    // 또는 attrMap.removeNamedItem(attrNode.getName())
            	}
            	capital.setAttribute("id", id);//id속성 추가
            }
            
            // 2-5 <country>의 모든 속성들을 삭제
            NamedNodeMap attrMap = country.getAttributes();
            while(attrMap.getLength() > 0) {
            	Attr attrNode = (Attr)attrMap.item(0);
                country.removeAttributeNode(attrNode);
            }
            
            // 2-6 <country> 외의 다른 노드들은 모두 삭제
            for (Node ch1 = mondial.getFirstChild(); ch1 != null;) {
                if (!ch1.getNodeName().equals("country")) {
                    Node nextChild = ch1.getNextSibling();
                    mondial.removeChild(ch1);
                    ch1 = nextChild;
                } else
                    ch1 = ch1.getNextSibling();
            }
            
        }
		
	} 
	
	public static void computePopulationsOfContinents(Element mondial) {
		NodeList list = mondial.getElementsByTagName("country");
		for(int i = 0; i < list.getLength(); i++) {
			Node country = list.item(i);
			
			Node popNode = country.getFirstChild().getNextSibling();
			if(!popNode.getNodeName().equals("population"))
				return;
			
			String popStr = popNode.getTextContent();
			long population = Long.parseLong(popStr);
			
			Node node = popNode.getNextSibling();
			while(node != null && !node.getNodeName().equals("encompassed"))
				 node = node.getNextSibling(); // <encompassed>가 발견될 때까지 이동
			
			double maxPer = 0.0;// percentage의 max 값을 저장할 변수
			String maxContinent = null;
			while(node.getNodeName().equals("encompassed")) {
				Element encompassed = (Element)node;
				String percentage = encompassed.getAttribute("percentage");
				double per = Double.parseDouble(percentage); 
				
				if(per > maxPer) {
					maxPer = per;
					maxContinent = encompassed.getAttribute("continent");
				}
				node = node.getNextSibling();
			}
			 switch (maxContinent) {
             case "africa":
                 pop[0] += population;
                 break;
             case "america":
                 pop[1] += population;
                 break;
             case "asia":
                 pop[2] += population;
                 break;
             case "australia":
                 pop[3] += population;
                 break;
             case "europe":
                 pop[4] += population;
         }
		}
		
		// 계산된 각 대륙의 인구를 출력
		printPopulationsOfContinents();
	}



	public static void printPopulationsOfContinents() {
		NumberFormat formatter = NumberFormat.getInstance();
		System.out.println("Populations of the Continents");
		System.out.println("-----------------------------");
        long total = 0L;
        for(int i = 0; i < pop.length; i++) {
        	total += pop[i];
        }
        for (int i = 0; i < pop.length; i++) {
            System.out.println(continent[i] + "의 인구: " + formatter.format(pop[i]) + "명("
                    + formatter.format(pop[i] * 100.0 / total) + "%)");
        }
        System.out.println("-----------------------------");
        System.out.println("합계: " + formatter.format(total) + "명");
        System.out.println();
        
	}
	static Map<String, Long> religions = new HashMap<String, Long>();
	
	public static void computeBelieversOfReligions(Element mondial) {
		NodeList list = mondial.getElementsByTagName("country");
		
		for(int i = 0; i < list.getLength(); i++) {
			Element country = (Element)list.item(i);
			
			Node popNode = country.getFirstChild().getNextSibling();; // <population>?
			if (!popNode.getNodeName().equals("population")) // <population>이 아닌 다른 엘리먼트
				return;
			
			long population = Long.parseLong(popNode.getTextContent()); // population의 값을 구해서 long type으로 변환
			
			Node node = popNode;
			do {// <religions>가 발견될 때까지 이동
				node = node.getNextSibling();
			}while(node != null && !node.getNodeName().equals("religions"));
			
			if(node == null) break;// <religions>가 없는 국가
			
			while(node.getNodeName().equals("religions")) {// 각 <religions>에 대해 처리
				String percentage = ((Element)node).getAttribute("percentage");
				double per = Double.parseDouble(percentage);
				long NumOfBelievers = (long)(population  * per / 100);
				String religionName = node.getTextContent();
				
				if(religions.containsKey(religionName)) {
					religions.put(religionName, religions.get(religionName) + NumOfBelievers);
				}else { // 새로운 종교
                    religions.put(religionName, NumOfBelievers);
                }
				node = node.getNextSibling();
			}
		}
		
		NumberFormat formatter = NumberFormat.getInstance();
		System.out.println("Populations of the Religions");
	    System.out.println("-----------------------------");
	    // religions map에 저장된 entry들을 신자 수의 역순으로 정렬하기 위한 코드
	    Set<Map.Entry<String, Long>> religionSet = religions.entrySet(); // map entry들의 집합
        List<Map.Entry<String, Long>> religionList = new LinkedList<Map.Entry<String, Long>>(religionSet); // map entry들의 리스트
	
        // map entry들의 비교를 위한 Comparator 정의 (anonymous class로 정의 및 객체 생성)
        Comparator<Map.Entry<String, Long>> religionComparator = new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Entry<String, Long> rel1, Entry<String, Long> rel2) {
                return (int) (rel2.getValue() - rel1.getValue()); // 내림차순을 위한 비교
            }
        };
 
        // religionList의 entry들을 내림차순으로 정렬
        Collections.sort(religionList, religionComparator);
 
        // 정렬된 리스트의 entry들을 차례대로 출력
        long total = 0L;
        Iterator<Map.Entry<String, Long>> iter = religionList.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Long> religion = iter.next();
            long NumOfBelievers = religion.getValue();
            System.out.println(religion.getKey() + " 신자: " + formatter.format(NumOfBelievers) + "명");
            total += NumOfBelievers;
        }
 
        System.out.println("-----------------------------");
        System.out.println("합계: " + formatter.format(total) + "명");
        System.out.println();
	
	}

}