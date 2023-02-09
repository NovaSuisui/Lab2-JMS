/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prime_server;

/**
 *
 * @author SK
 */
public class PrimeCount {
    public int calculatePrime(int startNum, int endNum){
        int countNum = 0;
        for(int i = startNum; i <= endNum; i++){
            if(isPrime(i)){
                countNum++;
            }
        }
        return countNum;
    }
    
    private boolean isPrime(int n) {
        int i;
        for (i = 2; i*i <= n; i++) {
            if ((n % i) == 0) {
            return false;
            }
        }
        return true;
    }
}
