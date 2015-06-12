/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fetch;

/**
 *
 * @author Vital
 */
public class Fetch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Thread getMess = new Thread(new GetMess("pop.rambler.ru", "995", "test_testovic", "brigada"));
        getMess.start();
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(getMess, 0, 10, TimeUnit.SECONDS);
    }
    
}
