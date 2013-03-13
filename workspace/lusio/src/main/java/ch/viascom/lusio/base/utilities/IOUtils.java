package ch.viascom.lusio.base.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
public class IOUtils {
	// 4 KB buffer size
	public static final int IO_BUFFER_SIZE = 4 * 1024;
	

	public static ByteArrayOutputStream toByteArrayOutputStream(InputStream inputStream, boolean closeStream) throws IOException {
		if (inputStream == null)
			return null;

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			int nRead;
			byte[] data = new byte[4096];
	
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			
			buffer.flush();
		}
		finally {
			buffer.close();
			inputStream.close();
		}
		
		return buffer;
	}
	
	public static String toString(InputStreamReader inputStreamReader, boolean closeStream) throws IOException {
		if (inputStreamReader == null)
			return "";
	
		try {
		    Writer writer = new StringWriter();
		    try {
			    char[] buffer = new char[4096];
		
		        Reader reader = new BufferedReader(inputStreamReader);
		        
		        int n;
		        while ((n = reader.read(buffer, 0, buffer.length)) != -1) {
		            writer.write(buffer, 0, n);
		        }
	        	
		        writer.flush();
	        	
			    return writer.toString();
		    } finally {
		    	writer.close();
		    }
		} finally {
			if (closeStream)
				inputStreamReader.close();
		}
	}

	public static String toString(InputStream reader, String encoding, boolean closeStream) throws IOException {
		if (reader == null)
			return "";

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    try {
			    byte[] buffer = new byte[1024];
		        
		        int n;
		        while ((n = reader.read(buffer, 0, buffer.length)) != -1) {
		        	baos.write(buffer, 0, n);
		        }
	
		        baos.flush();
		        
			    return baos.toString(encoding);
		    } finally {
		    	baos.close();
		    }
		} finally {
			if (closeStream)
				reader.close();
		}
	}
	
	public static void writeToFile(File file, String content, String encoding) throws IOException {
		BufferedReader in = new BufferedReader(new StringReader(content));
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
			try {
				char[] buffer = new char[1024];
				
				int n;
				while ((n = in.read(buffer, 0, buffer.length)) != -1)
					out.write(buffer, 0, n);
				
				out.flush();
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}
	
	public static void writeToFile(String path, String content, String encoding) throws IOException {
		writeToFile(new File(path), content, encoding);
	}
	
	public static void writeToFile(File file, byte[] content) throws IOException {
		FileOutputStream output = new FileOutputStream(file);
		try {
			if (content == null)
				content = new byte[0];
			
			output.write(content);
			output.flush();
		} finally {
			output.close();
		}
	}

	public static void writeToFile(String path, byte[] content) throws IOException {
		writeToFile(new File(path), content);
	}
	
	public static String readFileString(String path, String encoding) throws IOException {
		return readFileString(new File(path), encoding);
	}
	
	public static String readFileString(File file, String encoding) throws IOException {
		return IOUtils.toString(new FileInputStream(file), encoding, true);
	}

	public static void copy(InputStream input, OutputStream output, boolean closeInput) throws IOException {
		try {
			byte[] b = new byte[IO_BUFFER_SIZE];  
			int read;  
			while ((read = input.read(b)) != -1) {  
				output.write(b, 0, read);  
			}
		}
		finally {
			if (closeInput && input != null)
				input.close();
		}
	}
}
