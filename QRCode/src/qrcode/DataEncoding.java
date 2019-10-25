package qrcode;

import java.nio.charset.StandardCharsets;
import reedsolomon.ErrorCorrectionEncoding;

public final class DataEncoding {

	/**test
	 * @param input test
	 * @param version
	 * @return
	 */
	
	public static void main(String[] args) {
		int[] inputBytes = encodeString(Main.INPUT, QRCodeInfos.getMaxInputLength(Main.VERSION));
		int[] encodedData = addInformations(inputBytes);
		int[] data = fillSequence(encodedData, QRCodeInfos.getCodeWordsLength(Main.VERSION));
		int[] dataCorrection = addErrorCorrection(data, QRCodeInfos.getECCLength(Main.VERSION));
		bytesToBinaryArray(dataCorrection);
	}
	
	public static boolean[] byteModeEncoding(String input, int version) {
		// TODO Implementer
		
		return null;
	}

	/**
	 * @param input
	 *            The string to convert to ISO-8859-1
	 * @param maxLength
	 *          The maximal number of bytes to encode (will depend on the version of the QR code) 
	 * @return An array that represents the input in ISO-8859-1. The output is
	 *         truncated to fit the version capacity
	 */
	public static int[] encodeString(String input, int maxLength) {
		// TODO Implementer
		byte[] tabByte = input.getBytes(StandardCharsets.ISO_8859_1);
		
		if(maxLength >= tabByte.length ) { 
			maxLength = tabByte.length;
		} // si le message prend moins de place que maxLength, r�duit la taille des arrays
		
		int[] inputBytes = new int[maxLength]; //ne prend que les maxLength premiers octets de tabVyte[]
		
		for(int i = 0 ; i < maxLength; ++i) {
			inputBytes[i] = tabByte[i] & 0xFF; 
		}

		return inputBytes;
	}

	/**
	 * Add the 12 bits information data and concatenate the bytes to it
	 * 
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	public static int[] addInformations(int[] inputBytes) {
		// TODO Implementer
		int lgth = inputBytes.length & 0xFF ; //49
		int prefix = 0b0100 ;
		
		int[] encodedData = new int[inputBytes.length + 2] ;
		
		encodedData[0] = prefix << 4  | (lgth & 0b1111_0000) >> 4 ; // Conjonction logique(&) entre lgth et un octet o� ses 4 bits de poids faibles sont � 0, pour �tre s�r de ne prendre que les 4 bits de poids fort
		// 0100_0000 or 0000_0011  = 0100_0011
		
		encodedData[1] =  (lgth & 0b0000_1111) << 4 | inputBytes[0] >> 4 ; //0001_0000 or 0000_0101 = 0001_0101
		
		
		for(int i = 2 ; i < encodedData.length - 1; ++i) { // le dernier �l�ment sera ajout� hors de la boucle pour ne pas que l'algorithme aille chercher un 50e �l�ment (hors des bornes) de inputBytes
			
			int heavyB = inputBytes[i-2] & 0b0000_1111 ; // Prend seulement la moiti� n�cessaire et la d�cale pour pouvoir combiner les deux ( heavyB et lightB)
			int lightB = inputBytes[i-1] /*& 0b1111_0000*/;
			encodedData[i] =  heavyB << 4  | lightB >> 4 ;
			
		} 
		
		encodedData[encodedData.length - 1] = (inputBytes[inputBytes.length - 1] & 0b0000_1111) << 4  | 0b0000  ;
		
		
		return encodedData;	
	}

	/**
	 * Add padding bytes to the data until the size of the given array matches the
	 * finalLength
	 * 
	 * @param encodedData
	 *            the initial sequence of bytes
	 * @param finalLength
	 *            the minimum length of the returned array
	 * @return an array of length max(finalLength,encodedData.length) padded with
	 *         bytes 236,17
	 */
	public static int[] fillSequence(int[] encodedData, int finalLength) {
		// TODO Implementer
		
		if(encodedData.length < finalLength) {
			
			
			int[] data = new int[finalLength];
			
			for(int i = 0 ; i < encodedData.length ; ++i) {
				data[i] = encodedData[i];
			}
			int j=0;
			
			for(int i = encodedData.length ; i < finalLength ; ++i ) {
				
				if(j == 0) {
					data[i] = 236 & 0xFF;
					j = 1;
				} else if(j == 1) {
					data[i] = 17 & 0xFF;
					j = 0;
				}
				
			}
			
			return data;
			
			
		} 
		
		return encodedData;
	}

	/**
	 * Add the error correction to the encodedData
	 * 
	 * @param encodedData
	 *            The byte array representing the data encoded
	 * @param eccLength
	 *            the version of the QR code
	 * @return the original data concatenated with the error correction
	 */

	public static int[] addErrorCorrection(int[] encodedData, int eccLength) {
		// TODO Implementer
		
		int[] correctionBytes = ErrorCorrectionEncoding.encode(encodedData , eccLength);
		//tableau de taille �gale � la somme de celle des autres tableaux
		
		int[] dataCorrection = new int[correctionBytes.length + encodedData.length]; 
		
		for(int i = 0; i < dataCorrection.length ; ++i ) {
			
			//Une fois que la valeur de i d�passe la taille du tableau comprenant les octet de encodedData, ce sont les eccLength octets de dataCorrection qui remplissent la fin du tableau final dataCorrection. 
			if( i < encodedData.length ) {
				dataCorrection[i] = encodedData[i] ;
			} else {
				dataCorrection[i] = correctionBytes[i-encodedData.length];
			}
			
		}
			
		return dataCorrection;
	}

	/**
	 * Encode the byte array into a binary array represented with boolean using the
	 * most significant bit first.
	 * 
	 * @param data
	 *            an array of bytes
	 * @return a boolean array representing the data in binary
	 */
	public static boolean[] bytesToBinaryArray(int[] data) {
		// TODO Implementer
		
		int[][] binValue = getBinValue(data);
		boolean[] binaryArray = new boolean[data.length*8];
		int i = 0;
		
		for(int k = 0 ; k < data.length ; ++k) {
			
			for(int j = 0 ; j < binValue[k].length ; ++j) {
				//mets chaque array de 8 bits correspondant � un �l�ment de data[] sous forme de valeur bool�enne ("projette" le tableau en 2d dans un tableau "simple" en bool�en)
				if(binValue[k][j] == 1) {
					binaryArray[i] = true;
				} else {
					binaryArray[i] = false;
				}
				++i;
			}
			
		}

		return binaryArray;
	}
	
	public static int[][] getBinValue(int[] data) {
		
		int[] bitTbl = getBitIsolator();
		int[][]binValue = new int[data.length][8];
		
		for(int i = 0 ; i < data.length ; ++i) {
			//fait un array de 8 �l�ment pour stocker les 8 bits d'un entier. Puis r�p�te tout �a pour chaque �l�ment de data[]
			for(int j = 0 ; j< 8 ; ++j) {
				binValue[i][j] = (bitTbl[j] & data[i]) >> (7-j) ;
				//d�cale la valeur de 7-j pour que la valeur soit toujours soit 0, soit 1 (bitTbl commence � 1000_0000)
			}
			
		}
		
		return binValue;
	}
	
	public static int[] getBitIsolator() {
		
		int[] bitIsolator = new int[8]; 
		
		//rempli un tableau d'octet avec 1 bit par octet � une place diff�rente � chaque fois pour utiliser la m�me "astuce" que dans la m�thode addInformations()
		for(int i = 0; i < 8 ; ++i) {
			bitIsolator[i] = 0b1000_0000 >> i;
		} //tableau commence � 1000_000 et fini � 0000_0001 
		
		return bitIsolator;
	}
	
	
	//Pour tester/afficher la valeur en binaire du message encod� en ayant un output sous format binaire (dans un string) 
	public static String toBinaryStr(int X) { 
		String binaryX = Integer.toBinaryString(X);
		int length = binaryX.length();
		
		for(int i = 0; i < 8 - length; i ++) {
			binaryX = "0" + binaryX;
		}
		
		//System.out.println(binaryX);
		
		return binaryX;
	}

	
}