package com.example.beatmakingapp;

// wav IO based on code by Evan Merz. Modified by The Beat Making App Team. 

import java.io.*;
import java.util.*;

import com.example.beatmakingapp.ProjectManager;
import com.example.beatmakingapp.Sound;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.provider.MediaStore;

public class WavIO {
	/*
	 * WAV File Specification FROM
	 * http://ccrma.stanford.edu/courses/422/projects/WaveFormat/ The canonical
	 * WAVE format starts with the RIFF header: 0 4 ChunkID Contains the letters
	 * "RIFF" in ASCII form (0x52494646 big-endian form). 4 4 ChunkSize 36 +
	 * SubChunk2Size, or more precisely: 4 + (8 + SubChunk1Size) + (8 +
	 * SubChunk2Size) This is the size of the rest of the chunk following this
	 * number. This is the size of the entire file in bytes minus 8 bytes for
	 * the two fields not included in this count: ChunkID and ChunkSize. 8 4
	 * Format Contains the letters "WAVE" (0x57415645 big-endian form).
	 * 
	 * The "WAVE" format consists of two subchunks: "fmt " and "data": The
	 * "fmt " subchunk describes the sound data's format: 12 4 Subchunk1ID
	 * Contains the letters "fmt " (0x666d7420 big-endian form). 16 4
	 * Subchunk1Size 16 for PCM. This is the size of the rest of the Subchunk
	 * which follows this number. 20 2 AudioFormat PCM = 1 (i.e. Linear
	 * quantization) Values other than 1 indicate some form of compression. 22 2
	 * NumChannels Mono = 1, Stereo = 2, etc. 24 4 SampleRate 8000, 44100, etc.
	 * 28 4 ByteRate == SampleRate * NumChannels * BitsPerSample/8 32 2
	 * BlockAlign == NumChannels * BitsPerSample/8 The number of bytes for one
	 * sample including all channels. I wonder what happens when this number
	 * isn't an integer? 34 2 BitsPerSample 8 bits = 8, 16 bits = 16, etc.
	 * 
	 * The "data" subchunk contains the size of the data and the actual sound:
	 * 36 4 Subchunk2ID Contains the letters "data" (0x64617461 big-endian
	 * form). 40 4 Subchunk2Size == NumSamples * NumChannels * BitsPerSample/8
	 * This is the number of bytes in the data. You can also think of this as
	 * the size of the read of the subchunk following this number. 44 * Data The
	 * actual sound data.
	 * 
	 * 
	 * NOTE TO READERS:
	 * 
	 * The thing that makes reading wav files tricky is that java has no
	 * unsigned types. This means that the binary data can't just be read and
	 * cast appropriately. Also, we have to use larger types than are normally
	 * necessary.
	 * 
	 * In many languages including java, an integer is represented by 4 bytes.
	 * The issue here is that in most languages, integers can be signed or
	 * unsigned, and in wav files the integers are unsigned. So, to make sure
	 * that we can store the proper values, we have to use longs to hold
	 * integers, and integers to hold shorts.
	 * 
	 * Then, we have to convert back when we want to save our wav data.
	 * 
	 * It's complicated, but ultimately, it just results in a few extra
	 * functions at the bottom of this file. Once you understand the issue,
	 * there is no reason to pay any more attention to it.
	 * 
	 * 
	 * ALSO:
	 * 
	 * This code won't read ALL wav files. This does not use to full
	 * specification. It just uses a trimmed down version that most wav files
	 * adhere to.
	 */

	// our private variables
	private String myPath;
	private long myChunkSize;
	private long mySubChunk1Size;
	private int myFormat;
	private long myChannels;
	private long mySampleRate;
	private long myByteRate;
	private int myBlockAlign;
	private int myBitsPerSample;
	private long myDataSize;

	// I made this public so that you can toss whatever you want in here
	// maybe a recorded buffer, maybe just whatever you want

	// get/set for the Path property
	public String getPath() {
		return myPath;
	}

	public void setPath(String newPath) {
		myPath = newPath;
	}

	// empty constructor
	public WavIO() {
		myPath = "";
	}

	// constructor takes a wav path
	public WavIO(String tmpPath) {
		myPath = tmpPath;
	}

	/**
	 * Create the data buffer for the entire track. Use this function before
	 * calling save.
	 */

	public byte[] createDataBuffer(PriorityQueue<Sound> trackSoundQueue,
			Context ctxt) {
		// myByteRate =
		ArrayList<Byte> dataBuffer = new ArrayList<Byte>();
		PriorityQueue<Sound> tempPQ = new PriorityQueue<Sound>(trackSoundQueue);
		Sound sound;
		String fileName = null;
		double differenceInMillisecs = 0;
		// for each sound in the track
		// get the starting position of the sound.
		// calculate the silence required.
		// add the silence bytes to the buffer
		// get the corresponding sound resource file
		// add the sound bytes to the databuffer
		double offset = 0;
		// int startTime = 0;
		double currentTime = 0; // shows the amount of track (in millisecs)
								// already processed.
		// traverse through each element of the track and calculate the track
		// length.
		while (tempPQ.size() > 0) {
			sound = tempPQ.poll();
			offset = sound.getOffset();
			if (offset > currentTime) {
				differenceInMillisecs = sound.getOffset() - currentTime;
				ArrayList<Byte> silenceBytes = new ArrayList<Byte>();
				Byte silence = 0;
				int numberofsilencebytes = (int) Math
						.ceil(((float) (myByteRate) / 1000.00)
								* differenceInMillisecs); // myByteRate = 28
															// bytes /sec = 28
															// /1000 bytes per
															// millisecs.
				for (int i = 0; i < numberofsilencebytes; i++) {
					silenceBytes.add(silence);
				}

				dataBuffer.addAll(silenceBytes);

			}

			fileName = getFileNameFromSound(sound);
			byte[] myMusicData = read(ctxt, fileName);
			for (int x = 0; x < myMusicData.length; x++) {
				dataBuffer.add(myMusicData[x]);

			}
			currentTime = sound.getOffset();

		}
		byte[] myMusicData = read(ctxt, fileName);
		for (int x = 0; x < myMusicData.length; x++) {
			dataBuffer.add(myMusicData[x]);

		}

		byte[] dataArray = new byte[dataBuffer.size()];
		for (int x = 0; x < dataBuffer.size(); x++) {
			dataArray[x] = dataBuffer.get(x);

		}

		return dataArray;
	}

	// read a wav file into this class

	private String getFileNameFromSound(Sound s) {

		String fileName = Global.filenames[s.getButtonId_i()][s.getButtonId_j()];
		fileName = fileName.substring(0, fileName.length() - 4);
		return fileName;

	}

	public byte[] read(Context ctxt, String fileName) {
		DataInputStream inFile = null;
		byte[] myData = null;
		byte[] tmpLong = new byte[4];
		byte[] tmpInt = new byte[2];

		try {
			// File projectDir = ctxt.getDir("Project1",Context.MODE_PRIVATE);
			// File myFile = new File(fileName);
			int resourceId = com.example.beatmakingapp.R.raw.class
					.getDeclaredField(fileName).getInt(new Integer(0));
			InputStream is = ctxt.getResources().openRawResource(resourceId);
			// File myFile = new File("");
			inFile = new DataInputStream((is));

			// System.out.println("Reading wav file...\n"); // for debugging
			// only

			String chunkID = "" + (char) inFile.readByte()
					+ (char) inFile.readByte() + (char) inFile.readByte()
					+ (char) inFile.readByte();

			inFile.read(tmpLong); // read the ChunkSize

			myChunkSize += byteArrayToLong(tmpLong);
			String format = "" + (char) inFile.readByte()
					+ (char) inFile.readByte() + (char) inFile.readByte()
					+ (char) inFile.readByte();

			// print what we've read so far
			// System.out.println("chunkID:" + chunkID + " chunk1Size:" +
			// myChunkSize + " format:" + format); // for debugging only

			String subChunk1ID = "" + (char) inFile.readByte()
					+ (char) inFile.readByte() + (char) inFile.readByte()
					+ (char) inFile.readByte();

			inFile.read(tmpLong); // read the SubChunk1Size

			mySubChunk1Size = byteArrayToLong(tmpLong);

			inFile.read(tmpInt); // read the audio format. This should be 1 for
									// PCM
			if (myFormat == 0) {
				myFormat = byteArrayToInt(tmpInt);
			}
			inFile.read(tmpInt); // read the # of channels (1 or 2)
			myChannels = byteArrayToInt(tmpInt);

			inFile.read(tmpLong); // read the samplerate
			if (mySampleRate == 0) {
				mySampleRate = byteArrayToLong(tmpLong);
			}
			inFile.read(tmpLong); // read the byterate
			if (myByteRate == 0) {
				myByteRate = byteArrayToLong(tmpLong);
			}
			inFile.read(tmpInt); // read the blockalign
			if (myBlockAlign == 0) {
				myBlockAlign = byteArrayToInt(tmpInt);
			}
			inFile.read(tmpInt); // read the bitspersample
			if (myBitsPerSample == 0) {
				myBitsPerSample = byteArrayToInt(tmpInt);
			}
			// print what we've read so far
			// System.out.println("SubChunk1ID:" + subChunk1ID +
			// " SubChunk1Size:" + mySubChunk1Size + " AudioFormat:" + myFormat
			// + " Channels:" + myChannels + " SampleRate:" + mySampleRate);

			// read the data chunk header - reading this IS necessary, because
			// not all wav files will have the data chunk here - for now, we're
			// just assuming that the data chunk is here
			String dataChunkID = "" + (char) inFile.readByte()
					+ (char) inFile.readByte() + (char) inFile.readByte()
					+ (char) inFile.readByte();

			inFile.read(tmpLong); // read the size of the data
			myDataSize = byteArrayToLong(tmpLong);

			// read the data chunk
			myData = new byte[(int) myDataSize];
			inFile.read(myData);

			// close the input stream
			inFile.close();
		} catch (Exception e) {
			return null;
		}

		return myData;
	}

	// write out the wav file
	public boolean save(Context ctxt, String fileName, byte[] myData) {
		try {
			File sdcard = Environment.getExternalStorageDirectory();

			File projectDir = new File(sdcard.getAbsolutePath()
					+ "/Beats/exported/");
			projectDir.mkdirs();
			// File projectDir = ctxt.getDir("/sdcard/",Context.MODE_APPEND);

			// File myFile = new File(projectDir, fileName);

			File myFile = new File(projectDir, fileName);
			// byte[] myData = read(ctxt,"test.wav");
			DataOutputStream outFile = new DataOutputStream(
					new FileOutputStream(myFile));
			ProjectManager pm = new ProjectManager();
			pm.createProject(ctxt);
			// write the wav file per the wav file format
			outFile.writeBytes("RIFF"); // 00 - RIFF
			// work around ---------------
			myChunkSize = 36 + myData.length;
			outFile.write(intToByteArray((int) myChunkSize), 0, 4);
			//----------------------------
			//outFile.write(intToByteArray((int) myChunkSize), 0, 4); // 04 - how
																	// big is
																	// the rest
																	// of this
																	// file?
			outFile.writeBytes("WAVE"); // 08 - WAVE
			outFile.writeBytes("fmt "); // 12 - fmt
			outFile.write(intToByteArray((int) mySubChunk1Size), 0, 4); // 16 -
																		// size
																		// of
																		// this
																		// chunk
			outFile.write(shortToByteArray((short) myFormat), 0, 2); // 20 -
																		// what
																		// is
																		// the
																		// audio
																		// format?
																		// 1 for
																		// PCM =
																		// Pulse
																		// Code
																		// Modulation
			outFile.write(shortToByteArray((short) myChannels), 0, 2); // 22 -
																		// mono
																		// or
																		// stereo?
																		// 1 or
																		// 2?
																		// (or 5
																		// or
																		// ???)
			outFile.write(intToByteArray((int) mySampleRate), 0, 4); // 24 -
																		// samples
																		// per
																		// second
																		// (numbers
																		// per
																		// second)
			outFile.write(intToByteArray((int) myByteRate), 0, 4); // 28 - bytes
																	// per
																	// second
			outFile.write(shortToByteArray((short) myBlockAlign), 0, 2); // 32 -
																			// #
																			// of
																			// bytes
																			// in
																			// one
																			// sample,
																			// for
																			// all
																			// channels
			outFile.write(shortToByteArray((short) myBitsPerSample), 0, 2); // 34
																			// -
																			// how
																			// many
																			// bits
																			// in
																			// a
																			// sample(number)?
																			// usually
																			// 16
																			// or
																			// 24

			long numberOfBytes = myData.length;
			outFile.writeBytes("data"); // 36 - data

			outFile.write(intToByteArray((int) numberOfBytes), 0, 4);
			outFile.write(myData); // 44 - the actual data itself - just a long
									// string of numbers
			outFile.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}

	// return a printable summary of the wav file
	public String getSummary() {
		// String newline = System.getProperty("line.separator");
		String newline = "<br>";
		String summary = "<html>Format: " + myFormat + newline + "Channels: "
				+ myChannels + newline + "SampleRate: " + mySampleRate
				+ newline + "ByteRate: " + myByteRate + newline
				+ "BlockAlign: " + myBlockAlign + newline + "BitsPerSample: "
				+ myBitsPerSample + newline + "DataSize: " + myDataSize
				+ "</html>";
		return summary;
	}

	// ===========================
	// CONVERT BYTES TO JAVA TYPES
	// ===========================

	// these two routines convert a byte array to a unsigned short
	public static int byteArrayToInt(byte[] b) {
		int start = 0;
		int low = b[start] & 0xff;
		int high = b[start + 1] & 0xff;
		return (int) (high << 8 | low);
	}

	// these two routines convert a byte array to an unsigned integer
	public static long byteArrayToLong(byte[] b) {
		int start = 0;
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = b[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		return accum;
	}

	// ===========================
	// CONVERT JAVA TYPES TO BYTES
	// ===========================
	// returns a byte array of length 4
	private static byte[] intToByteArray(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) (i & 0x00FF);
		b[1] = (byte) ((i >> 8) & 0x000000FF);
		b[2] = (byte) ((i >> 16) & 0x000000FF);
		b[3] = (byte) ((i >> 24) & 0x000000FF);
		return b;
	}

	// convert a short to a byte array
	public static byte[] shortToByteArray(short data) {
		return new byte[] { (byte) (data & 0xff), (byte) ((data >>> 8) & 0xff) };
	}

}