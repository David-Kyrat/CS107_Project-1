package qrcode;

public class MatrixConstruction {

	/*
	 * Constants defining the color in ARGB format
	 * 
	 * W = White integer for ARGB
	 * 
	 * B = Black integer for ARGB
	 * 
	 * both needs to have their alpha component to 255
	 */
	// TODO add constant for White pixel
	// TODO add constant for Black pixel
	
	final static int W = 0xFF_FF_FF_FF;
	final static int B = 0xFF_00_00_00;
	final static int A = 0xFF_00_00_FF;
	
	final static int finderSize = 7;
	final static int paddingSize = finderSize + 1;
	final static int allignmentSize = 5;

	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {

		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);

		return matrix;
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** PART 2 *********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 * 
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) {
		// TODO Implementer
		return null;

	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 * 
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	public static int[][] initializeMatrix(int version) {	
		int maxVersion = 4;
		
		for (int i = 0; i < maxVersion; i++) {
			
			if(version == i) {
				int qrSize = QRCodeInfos.getMatrixSize(i);
				int[][] matrix = new int[qrSize][qrSize];
				
				for (int j = 0; j < qrSize ; i ++) {			
					
					for (int k = 0; k < qrSize ; k ++) {				
						
						matrix[j][k] = A;			
						
					}			
				}
				
				
				return matrix;
			}				
		}	
		
		return null;
	
	}

	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 * 
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	public static void addFinderPatterns(int[][] matrix) {
		
		int [][] finderPattern = {{B,B,B,B,B,B,B} , {B,W,W,W,W,W,B} , {B,W,B,B,B,W,B} , {B,W,B,B,B,W,B}
		, {B,W,B,B,B,W,B}, {B,W,W,W,W,W,B}  , {B,B,B,B,B,B,B} , {W,W,W,W,W,W,W}};	
		
				
		// Top Left corner		
		PatternGenerator(matrix, 0 , 0, finderSize, finderPattern);
		PatternHorizontal(matrix, 0, finderSize, paddingSize);
		PatternVertical(matrix, finderSize, 0, paddingSize);
		
		// Bottom Left corner
		PatternGenerator(matrix, matrix.length - finderSize , 0, finderSize, finderPattern);	
		PatternHorizontal(matrix, 0, matrix.length - paddingSize, paddingSize);
		PatternVertical(matrix, matrix.length - paddingSize, 0, paddingSize);
		
		// Top Right corner
		PatternGenerator(matrix, 0, matrix.length - finderSize, finderSize, finderPattern );
		PatternHorizontal(matrix, matrix.length- paddingSize, finderSize, paddingSize);
		PatternVertical(matrix, finderSize, matrix.length - paddingSize, paddingSize);
		
	}
	
	// the starting position is the top left corner of the finderPattern
	public static void PatternGenerator(int[][] matrix, int startPosX, int startPosY, int PatternSize, int [][] finderPattern) {		
		
		for (int i = startPosY; i < startPosY + PatternSize ; i ++) {			
			
			for (int k = startPosX; k < startPosX + PatternSize; k ++) {				
				
				matrix[i][k] = finderPattern[i - startPosY][k - startPosX];					
			}			
		}
			
	}
	
	
	public static void PatternHorizontal(int[][] matrix, int startPosX, int startPosY, int lineLength) {
		
		for (int i = startPosX; i < startPosX + lineLength ; i ++) {			
			
			matrix[i][startPosY] = W;			
		}		
	}
	
	public static void PatternVertical(int[][] matrix, int startPosX, int startPosY, int lineLength) {
		
		for (int i = startPosY; i < startPosY + lineLength ; i ++) {			
			
			matrix[startPosX][i] = W;			
		}		
	
	}

	/**
	 * Add the alignment pattern if needed, does nothing for version 1
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            the version number of the QR code needs to be between 1 and 4
	 *            included
	 */
	public static void addAlignmentPatterns(int[][] matrix, int version) {		
		 if (version <= 1) {
			 return ;
		 }
		 
		 int [][] alignmentPattern = {{B,B,B,B,B} , {B,W,W,W,B} , {B,W,B,W,B} , {B,W,W,W,B}, {B,B,B,B,B}};
		 
		 PatternGenerator(matrix,  matrix.length - 9, matrix.length - 9, allignmentSize, alignmentPattern);	
		 
			 
	}
	
	/**
	 * Add the timings patterns
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 */
	public static void addTimingPatterns(int[][] matrix) {
		
		 // forming a vertical line
		 for (int i = finderSize; i < matrix.length - finderSize; i ++) {
			 if(i % 2 == 0) {
				 matrix[finderSize - 1][i] = B;
			 }
			 
			 if(i % 2 == 1) {
				 matrix[finderSize - 1][i] = W;
			 }
		 }
		 
		 // forming a horizontal line
		 for (int i = finderSize; i < matrix.length - finderSize; i ++) {
			 if(i % 2 == 0) {
				 matrix[i][finderSize - 1] = B;
			 }
			 
			 if(i % 2 == 1) {
				 matrix[i][finderSize - 1] = W;
			 }
		 }
			

	}
	

	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) {		
		matrix[8][matrix.length - 8] = B;	
	}

	/**
	 * Add the format information to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */
	public static void addFormatInformation(int[][] matrix, int mask) {
		boolean[] formatInformationBoolean = QRCodeInfos.getFormatSequence(mask);
		int[] formatInformationBit = new int[formatInformationBoolean.length];

		
		for (int i = 0; i < formatInformationBoolean.length; i ++) {			
			System.out.print(formatInformationBoolean[i]);
			if(formatInformationBoolean[i]) {
				formatInformationBit[i] = B;
			} else {
				formatInformationBit[i] = W;
			}			
		}
		//Top left
		formatInformationHorizontal(matrix, 0, finderSize + 1, 8, formatInformationBit, 0) ;
		formatInformationVertical(matrix,  finderSize + 1 , finderSize + 1, paddingSize + 1, formatInformationBit, 7) ;
		
		//Top right
		formatInformationHorizontal(matrix, matrix.length - paddingSize, finderSize + 1, 8, formatInformationBit, 7) ;
		formatInformationVertical(matrix,  finderSize + 1 , matrix.length - 1, paddingSize - 1, formatInformationBit, 0) ;
	}	
	
	static void formatInformationHorizontal(int[][] matrix, int startPosX, int startPosY, int lineLength, int[]informationPattern, int informationPatternIndex) {		
		
		for (int i = startPosX; i < startPosX + lineLength ; i ++) {			
			System.out.println("horizontal" + i);
			if(matrix[i][startPosY] != B && matrix[i][startPosY] != W) {			
				matrix[i][startPosY] = informationPattern[informationPatternIndex];	
				informationPatternIndex++;
			}
		}		
	}
	
	static void formatInformationVertical(int[][] matrix, int startPosX, int startPosY, int lineLength, int[]informationPattern, int informationPatternIndex) {		
		
		for (int i = startPosY; i > startPosY - lineLength ; i --) {			
			System.out.println("vertical" + i);
			if(matrix[startPosX][i] != B && matrix[startPosX][i] != W) {					
				matrix[startPosX][i] = informationPattern[informationPatternIndex];	
				informationPatternIndex++;
			}
		}		
	}
	

	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 * 
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) {
		// TODO Implementer
		return 0;
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask) {
		// TODO Implementer

	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** BONUS **********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * The mask is computed automatically so that it provides the least penalty
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);

		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 * 
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {
		// TODO BONUS
		return 0;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		//TODO BONUS
	
		return 0;
	}

}
