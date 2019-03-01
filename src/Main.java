/**
 * @author mazharul.islam
 *
 */





import chaotic.*;
import ourmethod.*;



public class Main {

	public static void main(String[] args) {
		///*
		//	chaoticHuffmanCompression huffman = new chaoticHuffmanCompression();
		for(Integer siz = 1024;siz<=2097152;siz*=2) {
			long mean1 = 0;
			long mean2 = 0;
			String inputFileNamme = "test-data/sizes/"+String.valueOf(siz)+".txt";
			String compressedFileName = "test-data/sizes/"+String.valueOf(siz)+".zip";
			String decompressedFileName = "test-data/sizes/"+String.valueOf(siz)+".again.txt";
			Huffman huffman = new Huffman();
			double t1 = 0.0,t2=0.0;
			for(int i=0;i<20;i++) {

				mean1 += huffman.compress(inputFileNamme, compressedFileName);
				mean2 += huffman.deCompress(compressedFileName,decompressedFileName);
				t1 +=mean1;
				t1 +=mean2;
			}

			//System.out.println(mean1/20.00+ " "+mean2/20.00);
			//System.out.println(mean1+ " "+mean2);
			//	/*
			mean1 = 0;
			mean2 = 0;
			chaoticHuffmanCompression chaotichuffman = new chaoticHuffmanCompression();
			for(int i=0;i<20;i++) {
				mean1 += chaotichuffman.compress(inputFileNamme, compressedFileName);
				mean2 += chaotichuffman.deCompress(compressedFileName,decompressedFileName);
				//	}
				t2 +=mean1;
				t2 +=mean2;
			}
			//	System.out.println(mean1/20.00+ " "+mean2/20.00);
			double sadResult = (t2-t1)/t2  ;
			System.out.println(siz+" "+sadResult*100+" %");
			//	*/
		}
	}
}



