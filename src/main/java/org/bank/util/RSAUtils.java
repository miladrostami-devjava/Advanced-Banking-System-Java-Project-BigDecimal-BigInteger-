package org.bank.util;


import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * <p>
 * Key generation and simple cryptographic operations
 * </p>
 */
public class RSAUtils {
        private KeyPair keyPair;

    public static class KeyPair {
        public final BigInteger modules;//n
        public final BigInteger pubExp;//e
        public final BigInteger privExp;//d

        public  KeyPair(BigInteger modules, BigInteger pubExp, BigInteger privExp) {
            this.modules = modules;
            this.pubExp = pubExp;
            this.privExp = privExp;
        }
    }


    /**
     * <p>
     * Generate RSA Key pair for demo
     * </p>
     */
    public static   KeyPair generateKeys(int bits) {

        SecureRandom rnd = new SecureRandom();
        //generate two distinct primes roughly bits/2 each
        BigInteger p = BigInteger.probablePrime(bits / 2, rnd);
        BigInteger q;
        do {
            q = BigInteger.probablePrime(bits / 2, rnd);
        } while (q.equals(p));

        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(65537);// common public exponent
        if (!phi.gcd(e).equals(BigInteger.ONE)) {
            //fallback if 65537 not coprime (extremely rare) ,choose small odd e
            e = BigInteger.valueOf(3);
            while (!phi.gcd(e).equals(BigInteger.ONE)) {
                e = e.add(BigInteger.TWO);
            }
        }
        BigInteger d = e.modInverse(phi);

        //this.keyPair = new KeyPair(n,e,d);
        BigInteger d1= e.modInverse(phi);

        return new KeyPair(n,e,d1);

    }


    public KeyPair getKeyPair(){
        if (keyPair == null){
            throw new IllegalArgumentException("No key pair generated yet ,Call generateKeys() first.");
        }
        return keyPair;
    }


    /**
     * <p>
     *     Encrypt (message modulus)
     * </p>
     * */
    public   BigInteger encrypt(BigInteger message,BigInteger pubExp,BigInteger modules){
        if (message.compareTo(modules) >= 0){
            throw new IllegalArgumentException("Message must be smaller than modules");
        }
        return message.modPow(pubExp,modules);
    }


    public  BigInteger decrypt(BigInteger cipher,BigInteger privExp,BigInteger modulus){
        return cipher.modPow(privExp,modulus);
    }

    /**
     * <p>
     *     Sign (message < modules)
     * </p>
     * */

    public static  BigInteger sign(BigInteger message,BigInteger privExp,BigInteger modulus){


        return message.modPow(privExp,modulus);
    }







}
