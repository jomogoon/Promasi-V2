package org.promasi.multiplayer.client.clientstate;

import java.net.ProtocolException;

import org.apache.commons.lang.NullArgumentException;
import org.promasi.multiplayer.AbstractClientState;
import org.promasi.multiplayer.ProMaSiClient;
import org.promasi.network.protocol.client.request.RequestBuilder;
import org.promasi.network.protocol.client.response.CreateNewGameResponse;
import org.promasi.network.protocol.client.response.InternalErrorResponse;
import org.promasi.network.protocol.client.response.WrongProtocolResponse;
import org.promasi.shell.playmodes.multiplayerscoremode.MultiPlayerScorePlayMode;
import org.promasi.ui.promasiui.multiplayer.MakeGameForm;

public class GameMasterClientState extends AbstractClientState {

	/**
	 * 
	 */
	MakeGameForm _makeGameForm;
	
	/**
	 * 
	 */
	private MultiPlayerScorePlayMode _playMode;
	
	/**
	 * 
	 * @param playMode
	 * @throws NullArgumentException
	 */
	public GameMasterClientState(MultiPlayerScorePlayMode playMode, ProMaSiClient client)throws NullArgumentException
	{
		if( playMode==null )
		{
			throw new NullArgumentException("Wrong argument playMode=null");
		}
		
		if( client==null )
		{
			throw new NullArgumentException("Wrong argument client=null");
		}
		
		_makeGameForm=new MakeGameForm(client);
		_makeGameForm.setVisible(true);
		_playMode=playMode;
	}

	@Override
	public void onReceive(ProMaSiClient client, String recData) throws NullArgumentException {
		try
		{
			Object object=RequestBuilder.buildRequest(recData);
			if(object instanceof CreateNewGameResponse)
			{
				changeClientState(client, new WaitingGameClientState(_playMode));
				_makeGameForm.setVisible(false);
				_makeGameForm.dispose();
			}
			else
			{
				client.sendMessage(new WrongProtocolResponse().toProtocolString());
				client.disconnect();
			}
		}
		catch(ProtocolException e)
		{
			client.sendMessage(new WrongProtocolResponse().toProtocolString());
			client.disconnect();
		}
		catch(NullArgumentException e)
		{
			client.sendMessage(new InternalErrorResponse().toProtocolString());
			client.disconnect();
		}
		catch(IllegalArgumentException e)
		{
			client.sendMessage(new InternalErrorResponse().toProtocolString());
			client.disconnect();
		}
	}
	
}
