package org.leolo.invitebot2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleCommandResult {
	
	Logger logger = LoggerFactory.getLogger(ConsoleCommandResult.class);
	
	private PrintWriter out;
	private Status status = Status.NOT_READY;
	private BufferedReader reader;
	private ByteArrayOutputStream bais;
	private boolean pmOnly = false;
	
	public ConsoleCommandResult(){
		bais = new ByteArrayOutputStream();
		out = new PrintWriter(bais);
		status = Status.INPUT;
	}
	
	public PrintWriter getOutput(){
		if(status != Status.INPUT)
			throw new IllegalStateException();
		return out;
	}
	
	public enum Status{
		NOT_READY,
		INPUT,
		OUTPUT;
	}
	
	public void doneOutput(){
		out.flush();
		if(status != Status.INPUT)
			throw new IllegalStateException();
		status = Status.NOT_READY;
		byte [] input = bais.toByteArray();
		reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input)));
		status = Status.OUTPUT;
	}
	
	public String nextLine(){
		if(status != Status.OUTPUT)
			throw new IllegalStateException();
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return line;
	}
	
	public void resetOutput(){
		if(status != Status.OUTPUT)
			throw new IllegalStateException();
		status = Status.NOT_READY;
		try {
			reader.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		byte [] input = bais.toByteArray();
		reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input)));
		status = Status.OUTPUT;
	}

	public void write(int c) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.write(c);
	}

	public void write(char[] buf, int off, int len) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.write(buf, off, len);
	}

	public void write(char[] buf) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.write(buf);
	}

	public void write(String s, int off, int len) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.write(s, off, len);
	}

	public void write(String s) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.write(s);
	}

	public void print(boolean b) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(b);
	}

	public void print(char c) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(c);
	}

	public void print(int i) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(i);
	}

	public void print(long l) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(l);
	}

	public void print(float f) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(f);
	}

	public void print(double d) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(d);
	}

	public void print(char[] s) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(s);
	}

	public void print(String s) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(s);
	}

	public void print(Object obj) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.print(obj);
	}

	public void println() {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println();
	}

	public void println(boolean x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public void println(char x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public void println(int x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public void println(long x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public void println(float x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public void println(double x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public void println(char[] x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public void println(String x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public void println(Object x) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		out.println(x);
	}

	public PrintWriter printf(String format, Object... args) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		return out.printf(format, args);
	}

	public PrintWriter printf(Locale l, String format, Object... args) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		return out.printf(l, format, args);
	}

	public PrintWriter format(String format, Object... args) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		return out.format(format, args);
	}

	public PrintWriter format(Locale l, String format, Object... args) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		return out.format(l, format, args);
	}

	public PrintWriter append(CharSequence csq) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		return out.append(csq);
	}

	public PrintWriter append(CharSequence csq, int start, int end) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		return out.append(csq, start, end);
	}

	public PrintWriter append(char c) {
		if(status != Status.INPUT)
			throw new IllegalStateException();
		return out.append(c);
	}

	public boolean isPmOnly() {
		return pmOnly;
	}

	public void setPmOnly(boolean pmOnly) {
		this.pmOnly = pmOnly;
	}
}
