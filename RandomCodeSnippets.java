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