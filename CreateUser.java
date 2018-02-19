package ext.bose.adminutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.DirContext;
import javax.naming.directory.Attributes;
import javax.naming.directory.Attribute;
import javax.naming.NamingException;
import wt.util.WTProperties;


public class CreateUser {

	private static Properties properties = null;
	
	private static String propertiesFile = "createuser.properties";
	private static String ldapUri = null;
	private static String admindn = null;
	private static String admincred = null;
	private static String usersContainer = null;
	private static String usercred = null;
	private static String language = null;

	

    public static void createUserinWindchillDS(String username,String mailId,String firstName,String lastName, String organization) throws IOException{

    WTProperties wtProp = WTProperties.getLocalProperties();
    admindn = properties.getProperty("adminuser");
    admincred = properties.getProperty("adminpassword");
    usersContainer = properties.getProperty("userscontainer");
    usercred = properties.getProperty("userpassword");
    ldapUri = wtProp.getProperty("wt.federation.ie.ldapServer");
    language = properties.getProperty("preferredlanguage");

    
    
    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.PROVIDER_URL, ldapUri);
    env.put( Context.SECURITY_PRINCIPAL, admindn );
    env.put( Context.SECURITY_CREDENTIALS, admincred );
    
    try {
	        DirContext ctx = new InitialDirContext(env);
	        Attributes attrs = new BasicAttributes(true);
	
	        Attribute objclass = new BasicAttribute("objectclass");
	        objclass.add("top");
	        objclass.add("inetorgperson");
	        objclass.add("organizationalPerson");
	        objclass.add("person");
	        
	        Attribute commonName = new BasicAttribute("cn");
	        commonName.add(firstName + " " + lastName);
	        System.out.println("I need to merge automatically");
	        Attribute surname = new BasicAttribute("sn");
	        surname.add(lastName);
	
	        Attribute userID = new BasicAttribute("uid");
	        userID.add(username);
	        
	        Attribute mail = new BasicAttribute("mail");
	        mail.add(mailId);
	        
	        Attribute org = new BasicAttribute("o");
	        org.add(organization);
	        
	        Attribute preferredLanguage = new BasicAttribute("preferredLanguage");
	        preferredLanguage.add(language);
	        
	        Attribute pwd = new BasicAttribute("userpassword");
	        pwd.add(usercred);
	
	        attrs.put(objclass);
	        attrs.put(commonName);
	        attrs.put(surname);
	        attrs.put(userID);
	        attrs.put(mail);
	        attrs.put(org);
	        attrs.put(preferredLanguage);
	        attrs.put(pwd);
	        
	
	        ctx.createSubcontext("uid="+username+","+usersContainer, attrs);
	        ctx.close();


    } catch (NamingException e) {
        e.printStackTrace();
    }


    }
    static
    {
        try {
        	    WTProperties wtProp = WTProperties.getLocalProperties();
	            InputStream resource = new FileInputStream(wtProp.getProperty("wt.home") + File.separator + "codebase" + File.separator + propertiesFile);
	            if (resource != null) {
	                properties = new Properties();
	                properties.load(resource);
	            } 
	            else {
	            	System.out.println("ERROR: Failed to read properties from file $WT_HOME/" + propertiesFile);
	            }
         } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("ERROR: Failed to read properties: " + e.toString());
         }
    }
 }


