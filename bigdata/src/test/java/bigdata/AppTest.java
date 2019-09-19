package bigdata;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    public static void main(String[] args) {
        boolean tudou = "http://www.tudou.com/programs/view/1TNBg_wSXCE/".contains("tudou");
        System.out.println(tudou);
    }
}
