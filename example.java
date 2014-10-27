public class test
{
	public static void main(String[] args)
	{
		int i = 0;
		while(i < 20)
		{
			System.out.println(i + "hello");
			if(i % 3 == 0)
			{
				System.out.println("hello");
			}
			i++;
		}
		thisIsATest(2, 3);
	}
	public static void thisIsATest(int num1, int num2)
	{
		System.out.println(num1 * num2 + "");
	}
}
