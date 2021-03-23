/** key generation
*1. key with parity drop(8,16,24,32,40,48,56,64)
*2. divided into left(28) and right(28)
*3. left shift and right shift(1,2,9,6 one shift, the other two shift)
*4. compression p box
*/

import java.lang.*;
class KeyGeneration
{	
//shift function
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
		System.out.print("left round"+round+":\t");
		for(int c1=0;c1<28;c1++)
		{
			System.out.print(keyleft_s[c1]);
		}
		System.out.print("\tright round"+round+":\t");
		for(int c2=0;c2<28;c2++)
		{
			System.out.print(keyright_s[c2]);
		}
		System.out.println();
		int [] sub_key=new int[56];
		for(int d=0;d<28;d++)
			{
				sub_key[d]=keyleft_s[d];
				sub_key[d+28]=keyright_s[d];
				
			}
		
			return sub_key;
	}
	
//compression P-box function
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
	
	
	
	public static void main(String[] args)
		{
// 1. Key string convert to binary
			
			byte [] Asc=args[0].getBytes();
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
			System.out.println("key binary:");
			for(int s=0; s<64; s++)
				{
					System.out.print(key_64[s]);
				}
			System.out.println();
//2. key with parity drop
			int[] paritybox={56,48,40,32,24,16,8,0,
							57,49,41,33,25,17,9,1,
							58,50,42,34,26,18,10,2,
							59,51,43,35,62,54,46,38,
							30,22,14,6,61,53,45,37,
							29,21,13,5,60,52,44,36,
							28,20,12,4,27,19,11,3};
		
			int[] key_56= new int[56];
			System.out.println("key after parity drop:");
			for(int x=0;x<56;x++)
				{
					
					key_56[x]=key_64[paritybox[x]];
					System.out.print(key_56[x]);
				
				}
			System.out.println();	
	
		
//3. divided and shift
			System.out.println("56key after shift:");
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
			
		int[][][] character=new int[16][12][4];
		
		
		for(int v3=0; v3<16; v3++)
		{
			for(int w3=0;w3<12;w3++)
				
			{	for(int x4=0;x4<4;x4++)
				{
				character[v3][w3][x4]=Sub_k[v3][w3*4+x4];
				}
			
			}
				
		}
		int[][]character1=new int [16][12];
		for(int x3=0; x3<16; x3++)
			
			{
				for(int x5=0;x5<12;x5++)
				{
				character1[x3][x5]=character[x3][x5][0]*8+character[x3][x5][1]*4+character[x3][x5][2]*2+character[x3][x5][3];
				}
				
			}
	
		char[] cha={'0','1','2','3','4','5',
		            '6','7','8','9','A','B',
					'C','D','E','F'};
		char[][] character2=new char[16][12];
		System.out.println("pass the compression box get the sub_key:");
		
		for(int y3=0;y3<16; y3++)
		{	
	
			System.out.print("sub_key"+y3+":\t");
			for(int t2=0;t2<48;t2++)
			{
				System.out.print(Sub_k[y3][t2]);
			}
			System.out.print("\tHex_round"+y3+":\t");
			for(int y4=0; y4<12;y4++)
			{
				
			character2[y3][y4]=cha[character1[y3][y4]];
			
			System.out.print(character2[y3][y4]);
			}
			System.out.println();
		}	
		
		System.out.println();			
   
 }
}