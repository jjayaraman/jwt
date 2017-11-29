package com.jai.jwt;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class HS256Example {

	static Algorithm algorithm;

	public HS256Example() {
		try {
			algorithm = Algorithm.HMAC256("secret");
		}
		catch (IllegalArgumentException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		HS256Example example = new HS256Example();

		String token = example.createToken();
		System.out.println(token);

		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(token);

		System.out.println("Algo : " + decodedJWT.getAlgorithm());

		System.out.println("header : " + decodedJWT.getHeader());
		System.out.println("Payload : " + decodedJWT.getPayload());
		System.out.println("Signature : " + decodedJWT.getSignature());

		// Payload
		Map<String, Claim> claims = decodedJWT.getClaims();
		Iterator<Entry<String, Claim>> entries = claims.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, Claim> entry = entries.next();
			System.out.println(entry.getKey() + "-" + entry.getValue().asString());
		}
	}

	private String createToken() {

		String token = null;
		try {

			Map<String, Object> header = new HashMap<String, Object>();
			header.put("typ", "JWT");

			token = JWT.create().withHeader(header).withSubject("1234567890").withClaim("name", "John Doe").withClaim("admin", true).sign(algorithm);

			// eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ

		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return token;
	}

}
