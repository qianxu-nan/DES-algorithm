import java.lang.*;
class EncPlain2
{
//key shift function
	public static int[] key56(int[] x,int round )
	{
		int[] keyleft=new int[28];
		int[] keyright=new int[28];
		for(int a=0; a<28; a++)
			{
				keyleft[a]=x[a];
				keyright[a]=x[a+28];
					
			}
		
		int []keyleft_s=new int[28];
		int []keyright_s=new int[28];
		
		if(round==0||round==1||round==8||round==15)
		{
			
			keyleft_s[27]=keyleft[0];
			keyright_s[27]=keyright[0];
			for(int b=0; b<27;b++)
			{
				keyleft_s[b]=keyleft[b+1];
				keyright_s[b]=keyright[b+1];
					
			}
		}
		else
		{
			
			keyleft_s[27]=keyleft[1];
			keyright_s[27]=keyright[1];
			keyleft_s[26]=keyleft[0];
			keyright_s[26]=keyright[0];
		
		for(int c=0; c<26;c++)
			{
				keyleft_s[c]=keyleft[c+2];
				keyright_s[c]=keyright[c+2];
					
			}
		}

		int [] sub_key=new int[56];
		for(int d=0;d<28;d++)
			{
				sub_key[d]=keyleft_s[d];
				sub_key[d+28]=keyright_s[d];
				
			}
		
			return sub_key;
	}
	
//key compression P-box function
	public static int[] subKey(int[] y)
	{
		int [] kct= {13,16,10,23,0,4,2,27,
					14,5,20,9,22,18,11,3,
					25,7,15,6,26,19,12,1,
					40,51,30,36,46,54,29,39,
					50,44,32,47,43,48,38,55,
					33,52,45,41,49,35,28,31
					};
		int[] K= new int[48];
		for(int e=0;e<48;e++)
			{
				K[e]=y[kct[e]];
			}
			return K;
			
	}
//encyrtion function

	public static int[] desFunction(int[] cp,String stry, int rnd)
	
	{
	//1. key's binary	
		byte [] Asc=stry.getBytes();
		int [][] key64=new int[8][8];
		for(int i=0; i<Asc.length;i++)
			{
				int temp = Asc[i];
				for (int j=0; j<8;j++)
					{   
						key64[i][7-j]= temp&1;
						temp =temp>>>1;
					}
  
	}
		int[] key_64=new int[64];
		for(int m = 0; m < 8; m ++)
		{
			for(int n = 0; n< 8; n ++) 
				{
					key_64[m * 8 + n] = key64[m][n];
                
				}
		}
			
    //2. key with parity drop
		int[] paritybox={56,48,40,32,24,17,8,0,
						 57,49,41,33,25,17,9,1,
						 58,50,42,34,26,18,10,2,
						 59,51,43,35,62,54,46,38,
						 30,22,14,6,61,53,45,37,
						 29,21,13,5,60,52,44,36,
						 28,20,12,4,27,19,11,3};
		
		int[] key_56= new int[56];
			
		for(int x=0;x<56;x++)
			{
				key_56[x]=key_64[paritybox[x]];
			}
				
	
    //3. divided and shift
		int[][] key=new int[16][56];
		key[0]=key56(key_56,0);
		for(int x1=1; x1<16; x1++)
			{
				key[x1]=key56(key[x1-1],x1);
				
			}
			
			
    //4. get sub_key of every round
		int[][]Sub_k=new int[16][48];
		for(int y1=0; y1<16; y1++)
			{
				Sub_k[y1]=subKey(key[y1]);
			}
			
			

        int[] Plain_left=new int[32];
		int[] Plain_right=new int[32];
		for(int b3=0;b3<32;b3++)
			{
				Plain_left[b3]=cp[b3];
				Plain_right[b3]=cp[b3+32];
			}
				
    //4. DES function

		//a. expansion permutation
		
		int[] EP={31,0,1,2,3,4,3,4,5,6,7,8,
				  7,8,9,10,11,12,11,12,13,14,15,16,
				  15,16,17,18,19,20,19,20,21,22,23,24,
				  23,24,25,26,27,28,27,28,29,30,31,0};
				  
		int[] Plain_R=new int[48];
		for(int c3=0;c3<48;c3++)
			{
				Plain_R[c3]=Plain_right[EP[c3]];
				
			}
			
			
		//b. Plain_R Xor K
		
		int [] Plain_RX=new int[48];
		for(int d3=0;d3<48;d3++)
		{
			Plain_RX[d3]=Sub_k[rnd][d3]^Plain_R[d3];
			
		}
		
		//c. plain_RX pass the S Box
		
		    //(1) Plain_RX is divided into 8 sb
		int[][]sb=new int[8][6];
		for(int e3=0;e3<8;e3++)
		{
			for(int f3=0;f3<6;f3++)
			{
				sb[e3][f3]=Plain_RX[f3+e3*6];
				
			}
			
		}
		    //(2)get the row index of the sbox
		int[] row = new int [8];
		for(int g3=0;g3<8;g3++)
		{
			row[g3]=sb[g3][0]*2+sb[g3][5];
			
		}
		
		    //(3)get the column index of the sbox
		
		int[] column = new int [8];
		for(int h3=0;h3<8;h3++)
		{
			column[h3]=sb[h3][1]*8+sb[h3][2]*4+sb[h3][3]*2+sb[h3][4];
			
		}
		
		    // (4)8 sbox
			
		int[][][]s_box={{{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
				         {0,15,7,4,14,2,13,10,3,6,12,11,9,5,3,8},
				         {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
				         {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}},
						
						{{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
				         {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
				         {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
				         {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}},
						{{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
				         {13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
				         {13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
				         {1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}},
						{{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
				         {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
				         {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
				         {3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}},
						{{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
				         {14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
				         {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
				         {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}}, 
						{{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
				         {10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
				         {9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
				         {4,3,2,12,9,5,15,10,11,14,1,7,10,0,8,13}}, 
						{{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
				         {13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
				         {1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
				         {6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}},
						{{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
				         {1,15,13,8,10,3,7,4,12,5,6,11,10,14,9,2},
				         {7,11,4,1,9,12,14,2,0,6,10,10,15,3,5,8},
				         {2,1,14,7,4,10,8,13,15,12,9,9,3,5,6,11}}};
		


			// (5). get the 8 new integer plaintext
			
		int[] pinteger = new int[8];
		for(int i3=0;i3<8;i3++)
		{
			pinteger[i3]=s_box[i3][row[i3]][column[i3]];
			
		}
				
		    // (6). convert pinteger to binary
			
		int[][]ps=new int[8][4];
		for(int j3=0;j3<8;j3++)
		{  
			
			for (int k3=0; k3<4;k3++)
			{
				ps[j3][3-k3]=pinteger[j3]&1;
				pinteger[j3]=pinteger[j3]>>>1;
			}
		}
			
		
		int[] pafters=new int[32];
		for(int n3 = 0; n3 < 8; n3 ++)
			{
				for(int o3 = 0; o3< 4; o3 ++) 
					{
						pafters[n3 * 4 + o3] = ps[n3][o3];
                
					}
			}
			
		
      //d. pass the straight pbox 
			
		int[] spbox={15,6,19,20,28,11,27,16,
		             0,14,22,25,4,17,30,9,
				     1,7,23,13,31,26,2,8,
				     18,12,29,5,21,10,3,24};
				   
		int[]pafterp=new int[32];
		
		for(int q3=0; q3<32; q3++)
			{
				pafterp[q3]=pafters[spbox[q3]];
				
			}
			
			
			
      //e. round
	  
		int[] plain_left_round=new int[32];
		int[] plain_right_round=new int[32];
		if(rnd==15)
		{
			for(int rr3=0; rr3<32; rr3++)
			{
				plain_left_round[rr3]=Plain_left[rr3]^pafterp[rr3];
				plain_right_round[rr3]=Plain_right[rr3];
				
			}
		}
		else 
		{
			for(int r3=0; r3<32; r3++)
				{
					plain_left_round[r3]=Plain_right[r3];
					plain_right_round[r3]=Plain_left[r3]^pafterp[r3];
					
				}
		
		}
		
		
		
		//************************************************
		int[][] character1=new int[8][4];
		int[][] character2=new int[8][4];
		
		
		
			for(int w33=0;w33<8;w33++)
				
			{	for(int x44=0;x44<4;x44++)
				{
				character1[w33][x44]=plain_left_round[w33*4+x44];
				character2[w33][x44]=plain_right_round[w33*4+x44];
				}
			
			}
				
		
		int[]character3=new int [8];
		int[]character4=new int [8];
		
				for(int x55=0;x55<8;x55++)
				{
				character3[x55]=character1[x55][0]*8+character1[x55][1]*4+character1[x55][2]*2+character1[x55][3];
				character4[x55]=character2[x55][0]*8+character2[x55][1]*4+character2[x55][2]*2+character2[x55][3];
				}
				
			
	
		char[] cha={'0','1','2','3','4','5',
		            '6','7','8','9','A','B',
					'C','D','E','F'};
		char[]character5=new char[8];
		char[]character6=new char[8];
		
		System.out.print("left_round"+":\t");
		for(int y33=0;y33<32; y33++)
		{	
			System.out.print(plain_left_round[y33]);
		}
		System.out.print("\tright_round"+":\t");
		for(int y333=0;y333<32; y333++)
		{	
			System.out.print(plain_right_round[y333]);
		}
		System.out.println();
		System.out.print("left Hex"+":\t");
		for(int x33=0;x33<8; x33++)
		{	
			character5[x33]=cha[character3[x33]];
			System.out.print(character5[x33]);
		}
		System.out.print("\t			right Hex"+":\t");
		for(int x333=0;x333<8; x333++)
		{	
			character6[x333]=cha[character4[x333]];
			System.out.print(character6[x333]);
		}
		System.out.println();
		System.out.println();
   
		
		
		
		//***********************************************
		int[] ciphertext=new int[64];
		
		for(int t3=0; t3<32; t3++)
		{
			ciphertext[t3]=plain_left_round[t3];
			ciphertext[t3+32]=plain_right_round[t3];
		}
		return ciphertext;
	}
	// encryptionfinal function
	
	// 1. plaintext string convert to binary
	
	public static void encFinal(String strx, String strz)
	{
		   
			byte [] Pasc=strx.getBytes();
			int [][] plain64=new int[8][8];
			for(int i1=0; i1<Pasc.length;i1++)
				{
					int temp1 = Pasc[i1];
					for (int j1=0; j1<8;j1++)
						{   
							plain64[i1][7-j1]= temp1&1;
							temp1 =temp1>>>1;
						}
  
				}
			int[] plain_64=new int[64];
			for(int m1= 0; m1 < 8; m1 ++)
				{
					for(int n1 = 0; n1< 8; n1 ++) 
						{
							plain_64[m1 * 8 + n1] = plain64[m1][n1];
                
						}
				}
			System.out.println("plaintext binary:");
			System.out.println();
			for(int s1=0; s1<64; s1++)
				{
					System.out.print(plain_64[s1]);
				}
			System.out.println();
			System.out.println();
    // 2. pass the initial P-box


			int[] IP={57,49,41,33,25,17,9,1,
					  59,51,43,35,27,19,11,3,
					  61,53,45,37,29,21,13,5,
					  63,55,47,39,31,23,15,7,
					  56,48,40,32,24,16,8,0,
					  58,50,42,34,26,18,10,2,
					  60,52,44,36,28,20,12,4,
					 62,54,46,38,30,22,14,6};
			int[] Plain_I=new int[64];
			System.out.println("the plaintext pass the initial box");
			System.out.println();
			for(int a3=0;a3<64;a3++)
			{
				Plain_I[a3]=plain_64[IP[a3]];
				System.out.print(Plain_I[a3]);
			}
			System.out.println();
			System.out.println();
	//3. divide two left(32), right(32)
			System.out.println("the cipher of every rounds:");
			System.out.println();
			int[] Plain_left=new int[32];
			int[] Plain_right=new int[32];
			for(int b3=0;b3<32;b3++)
			{
				Plain_left[b3]=Plain_I[b3];
				Plain_right[b3]=Plain_I[b3+32];
			}
			
			
			int[][]plain_round=new int[16][64];
			plain_round[0]=desFunction(Plain_I,strz,0);
			for(int z1=1;z1<16;z1++)
			{
				
					plain_round[z1]=desFunction(plain_round[z1-1],strz,z1);
				
			}
			
			System.out.println("the ciphertext binary after 16 round:\t");
			System.out.println();
			for(int z5=0;z5<64;z5++)
			{
				System.out.print(plain_round[15][z5]);
			}
			System.out.println();
			System.out.println();
			int[] fp={39,7,47,15,55,23,63,31,
					  38,6,46,14,54,22,62,30,
					  37,5,45,13,53,21,61,29,
					  36,4,44,12,52,20,60,28,
					  35,3,43,11,51,19,59,27,
					  34,2,42,10,50,18,58,26,
					  33,1,41,9,49,17,57,25,
					  32,0,40,8,48,16,56,24};
					  
			int[]ciphertextfinal=new int[64];
			System.out.println("the final ciphertext after the final P-box:\t");
			System.out.println();
			for(int z6=0;z6<64;z6++)
			{
				ciphertextfinal[z6]=plain_round[15][fp[z6]];
				System.out.print(ciphertextfinal[z6]);
				
			}
		System.out.println();
		System.out.println();
		
		System.out.println("the final ciphertext Hex:\t");
		System.out.println();
		int[][] character=new int[16][4];
		int[] character1=new int[16];
		
		for(int v3=0; v3<16; v3++)
		{
			for(int w3=0;w3<4;w3++)
			{
				character[v3][w3]=ciphertextfinal[v3*4+w3];
				
			
			}
				
		}
		
		for(int x3=0; x3<16; x3++)
			
			{
				character1[x3]=character[x3][0]*8+character[x3][1]*4+character[x3][2]*2+character[x3][3];
				
				
			}
	
		char[] cha={'0','1','2','3','4','5',
		            '6','7','8','9','A','B',
					'C','D','E','F'};
		char[] character2=new char[16];
		
		for(int y3=0;y3<16; y3++)
		{
			character2[y3]=cha[character1[y3]];
			System.out.print(character2[y3]);
		}
		
		System.out.println();
		System.out.println();
		}
	
	public static void main(String[] args)
		{
			encFinal(args[0],args[1]);

		}
	
}









