package org.phabricator;

import org.phabricator.conduit.ConduitException;
import org.phabricator.conduit.raw.Conduit;
import org.phabricator.conduit.raw.ConduitFactory;
import org.phabricator.conduit.raw.UserModule.QueryResult;
import org.phabricator.conduit.raw.UserModule.UserResult;

public class TestClass {

	public static void main(String[] args) {
		String apiToken = "api-fgzgm3c7opso7z42auep5abfw47x";
		String baseUrl = "http://ph.labs.h3.se/" ;
		Conduit conduit = ConduitFactory.createConduit(baseUrl, apiToken);
		
		try {
			QueryResult qr = conduit.user.query(null, null, null, null, null, 0, null);
			
			for (UserResult ur : qr) {
				System.out.println(ur.getRealName());
			}
		} catch (ConduitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
