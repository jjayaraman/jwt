package com.jai.jwt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.io.FileUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * 
 * 
 * 
 * @author jjayaraman
 *
 */
public class RS256Example {

	private static String fileBase = "src/main/resources/rsa";

	static String PUBLIC_KEY_FILE = "src/main/resources/rsa.pub";
	static String PRIVATE_KEY_FILE = "src/main/resources/rsa.key";

	static RSAPublicKey publicKey = null;
	static RSAPrivateKey privateKey = null;
	static Algorithm algorithm = null;

	public RS256Example() {

		try {
			// generateKeys();

			// The public key is saved in X.509
			byte[] bytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE));
			X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(bytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = (RSAPublicKey) keyFactory.generatePublic(encodedKeySpec);

			// The private key is saved in PKCS #8
			byte[] bytes2 = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));
			PKCS8EncodedKeySpec ks2 = new PKCS8EncodedKeySpec(bytes2);
			KeyFactory kf2 = KeyFactory.getInstance("RSA");
			privateKey = (RSAPrivateKey) kf2.generatePrivate(ks2);

			algorithm = Algorithm.RSA256(publicKey, privateKey);
		}
		catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	private static void generateKeys() throws NoSuchAlgorithmException, IOException, FileNotFoundException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);

		KeyPair kp = kpg.generateKeyPair();
		publicKey = (RSAPublicKey) kp.getPublic();
		privateKey = (RSAPrivateKey) kp.getPrivate();

		try (FileOutputStream out = new FileOutputStream(fileBase + ".key")) {
			out.write(kp.getPrivate().getEncoded());
		}

		try (FileOutputStream out = new FileOutputStream(fileBase + ".pub")) {
			out.write(kp.getPublic().getEncoded());
		}
	}

	public String generateJWT() {
		String token = null;
		try {
			token = JWT.create().withIssuer("auth0").sign(algorithm);
		}
		catch (JWTCreationException e) {
			e.printStackTrace();
		}
		return token;
	}

	public void verifyJWT(String token) {

		try {
			JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier instance
			DecodedJWT jwt = verifier.verify(token);

			System.out.println(jwt.getToken());
			System.out.println(jwt.getHeader());
			System.out.println(jwt.getPayload());
			System.out.println(jwt.getSignature());

			System.out.println(jwt.getAlgorithm());
			System.out.println(jwt.getType());

			System.out.println(jwt.getContentType());
			System.out.println(jwt.getId());
			System.out.println(jwt.getIssuer());
			System.out.println(jwt.getKeyId());
			System.out.println(jwt.getSubject());
			System.out.println(jwt.getExpiresAt());

		}
		catch (JWTVerificationException e) {
			// Invalid signature/claims
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		RS256Example rsa = new RS256Example();
		String token = rsa.generateJWT();

		rsa.verifyJWT(token);
//		rsa.verifyJWT(token + "kkk");
		
		
//		token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImpqIiwiaWF0IjoxNTIzMzc0NzY1LCJleHAiOjE1MjMzODE5NjUsInN1YiI6ImpqIn0.lrqg9HIj_LiKMjxrsU9dS_n0mcq5W1WMws5qRKQ6k46TWsweY_dbnuS6OkNI_gh8VZagmwj8OIZKHnZYi6Vg6XH4_AG76eMSWFJ0lyw7TP5pozsci3NDTr-7HPxBNLKoi79U-9fQmgsteRX3R5cPNTjJ5oGspreX9tExdweC4uyFqOJ3VYSg6lnSEzH7AziGR54ZhO_SVWshFWT7OkPIzTD0Ut0XfPSAQST6-zgdmtVf9mKtv25Pii48ekeB_1bzEbbq2kScwGmHTBK0AFNnAK4QDD8BfGyWxd7BCB4gFLlNkEd3XG7dK-9MLdzua0RWx-4dKylS6XmM_g9mDcro2bnkrdBVcjZDrhDZ3r3do8Dz9Qr7xJ-v7MNehZ759NqyGt9PBFhlV0fjhwdJxrwQqn1fLkvEgsI3yLHwU8IZ-EulcFq1V9KX5orb0wgwO0AbcclSWOmWQkA9eVsMgYRYeNJzdOjtghsKF8q2so2xDtGKTYppgDpqHxVC0B3XAs4JkVRCC-LvdlJlPsCIRk8TG0PDE_5VO6GQdbdFvoc2k_4qDI7XYSSFaxeVxB9KwPWnd4yAyZP_slCivI80D1ab8dnb3TK3GHuDxqGM6eAtWLIiHLbuIPMSY1HyNgLfJJA3tsUUEJ5Wqx-Db3ufKFWUrsPL5k_vhCC-AIMet_kUY-M";
//		rsa.verifyJWT(token);
		

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
