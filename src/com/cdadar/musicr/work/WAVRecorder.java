package com.cdadar.musicr.work;

import android.media.AudioRecord;

public class WAVRecorder {
	
	
	/*
	 * The WAV format
	 * 
	 * edian offset	field				size
	 * big		0	chunkid				4
	 * little	4	chunksize			4
	 * big		8	format				4
	 * big		12	subchunkid			4
	 * little	16	subchunk1size		4
	 * little	20	audioformat			2
	 * little	22	numchannels			2
	 * little	24	samplerate			4
	 * little	28	byterate			4
	 * little	32	blockalign			2
	 * little	34	bits/sample			2
	 * big		36	subchunk2id			4
	 * little	40	subchunk2size		4
	 * little	44	data		
	 * 
	 * 
	 * source https://ccrma.stanford.edu/courses/422/projects/WaveFormat/
	 * 
	 * 
	 */

}
