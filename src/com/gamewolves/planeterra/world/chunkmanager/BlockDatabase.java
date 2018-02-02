package com.gamewolves.planeterra.world.chunkmanager;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BlockDatabase {
	
	private static final String BLOCKDATA_FILE = "res/blocks/blocks.xml"; //String to the BlockData file
	
	private static BlockData[] blockDatabase; //The majestic Block Database
	
	/**
	 * Loads the XML Data from BLOCKDATA_FILE and loads it into an Array of BlockData Objects
	 */
	public static void initializeDatabase() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document file = documentBuilder.parse(BLOCKDATA_FILE);
			NodeList blocks = file.getElementsByTagName("block");
			blockDatabase = new BlockData[blocks.getLength()];
			
			for (int i = 0; i < blocks.getLength(); i++) {
				Node blockNode = blocks.item(i); 
				
				if (blockNode.getNodeType() == Node.ELEMENT_NODE) {
					///////BLOCK///////
					Element block = (Element) blockNode;
					
					//ID
					int ID = Integer.parseInt(block.getAttribute("id"));
					
					//NAME
					String name = ((Element) block.getElementsByTagName("name").item(0)).getAttribute("name");
					
					//TEXTUREINDICES
					int[] textureIndices = new int[(block.getElementsByTagName("textureIndices").item(0)).getChildNodes().getLength() - 7];
					int indexInt = 0;
					for (int j = 0; j < textureIndices.length + 7; j++) {
						if ((block.getElementsByTagName("textureIndices").item(0)).getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
							Element index = (Element)(block.getElementsByTagName("textureIndices").item(0)).getChildNodes().item(j);
							textureIndices[indexInt] = Integer.parseInt(index.getAttribute("index"));
							indexInt++;
						}
					}
					
					//TRANSPARENCY
					boolean isTransparent = Integer.parseInt(((Element) block.getElementsByTagName("transparency").item(0)).getAttribute("transparent")) == 1 ? true : false;
					
					blockDatabase[i] = new BlockData(ID, name, textureIndices, isTransparent);
					///////BLOCK///////
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Finds the BlockData for a specific ID in the Database
	 * @param ID ID of the BlockData in the Database
	 * @return The BlockData with the ID
	 */
	public static BlockData getBlock(int ID) {
		if (ID >= blockDatabase.length)  {
			throw new IndexOutOfBoundsException("Item of this index not in Database!");
		}
		
		return blockDatabase[ID];
	}
}
