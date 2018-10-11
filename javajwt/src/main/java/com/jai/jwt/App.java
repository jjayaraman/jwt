package com.jai.jwt;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.algorithms.Algorithm;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		try {
			Algorithm algorithmHS = Algorithm.HMAC256("secret");

			
		}
		catch (IllegalArgumentException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		System.out.println("Hello World!");
	}
}
