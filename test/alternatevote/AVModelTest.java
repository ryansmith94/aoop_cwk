/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alternatevote;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ryansmith
 */
public class AVModelTest {
    private AVModel instance;
    
    @Before
    public void setUp() {
        instance = new AVModel();
    }
    
    @Test
    public void testStartCounting() {
        System.out.println(System.getProperty("user.dir"));
    }
    
    @Test
    public void testRedistributeTie() {
        
    }
    
    @Test
    public void testRedistributeWin() {
        
    }
    
}
