WindowBuilderLogIn logIn=new WindowBuilderLogIn();
		logIn.setVisible(true);
		logIn.setAlwaysOnTop(true);
		setEnabled(false);
		
		//wait for login
		while (true){
			if (logIn.logged_in){
				user_logged_in=logIn.user_logged_in;
				setEnabled(true);
				logIn.dispose();
				break;
			}
		}
		
		
		
		AudioInputStream audioStream;
				try {
					audioStream = AudioSystem.getAudioInputStream(new File(audio));
					AudioFormat audioFormat;
				    SourceDataLine sourceLine;
					
					audioFormat = audioStream.getFormat();

			        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
					
			        sourceLine = (SourceDataLine) AudioSystem.getLine(info);
		            sourceLine.open(audioFormat);
		            
		            sourceLine.start();
			        
		        } catch (Exception e1){
		            e1.printStackTrace();
		            System.exit(1);
		        }

		       
				
				InputStream in = new FileInputStream(audio);
			    // create an audiostream from the inputstream
			    AudioStream audioStream = new AudioStream(in);
			    // play the audio clip with the audioplayer class
			    AudioPlayer.player.start(audioStream);