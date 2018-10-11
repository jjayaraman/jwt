package com.jai.jwt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.io.FileUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

/**
 * Uses existing private & public keys
 * 
 * @author jjayaraman
 *
 */
public class RS256Example2 {

	public static void main(String[] args) {

		String PUBLIC_KEY_FILE = "src/main/resources/jwt.pub";
		String PRIVATE_KEY_FILE = "src/main/resources/jwt";

		RSAPublicKey publicKey = null;
		RSAPrivateKey privateKey = null;

		try {
			// The public key is saved in X.509
			byte[] bytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE));
			X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			publicKey = (RSAPublicKey) kf.generatePublic(ks);

			// The private key is saved in PKCS #8
			byte[] bytes2 = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));
			PKCS8EncodedKeySpec ks2 = new PKCS8EncodedKeySpec(bytes2);
			KeyFactory kf2 = KeyFactory.getInstance("RSA");
			privateKey = (RSAPrivateKey) kf2.generatePrivate(ks2);

		}
		catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}

		try {
			Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			String token = JWT.create().withIssuer("auth0").sign(algorithm);
			System.out.println("RSA Token : " + token);
		}
		catch (JWTCreationException e) {
			// Invalid Signing configuration / Couldn't convert Claims.
			e.printStackTrace();
		}

	}

	private static String readFile(String fileName) {
		try {
			String privateKey = FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8);
			System.out.println(privateKey);

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
