/**
 * 
 */
package org.promasi.multiplayer.client.clientstate;

import java.net.ProtocolException;

import javax.naming.ConfigurationException;

import org.apache.commons.lang.NullArgumentException;
import org.promasi.communication.Communicator;
import org.promasi.communication.ICommunicator;
import org.promasi.model.Company;
import org.promasi.multiplayer.AbstractClientState;
import org.promasi.multiplayer.ProMaSiClient;
import org.promasi.network.protocol.client.request.RequestBuilder;
import org.promasi.network.protocol.client.request.StartGameRequest;
import org.promasi.network.protocol.client.response.InternalErrorResponse;
import org.promasi.network.protocol.client.response.WrongProtocolResponse;
import org.promasi.shell.playmodes.multiplayerscoremode.MultiPlayerScorePlayMode;
import org.promasi.ui.promasiui.multiplayer.WaitingForGameFrame;

/**
 * @author m1cRo
 *
 */
public class WaitingGameClientState extends AbstractClientState {
	
	/**
	 * 
	 */
	private MultiPlayerScorePlayMode _playMode;
	
	/**
	 * 
	 */
	private WaitingForGameFrame _waitingForm;
	
	/**
	 * 
	 * @param playMode
	 * @param gameStory
	 * @throws NullArgumentException
	 */
	public WaitingGameClientState(MultiPlayerScorePlayMode playMode)throws NullArgumentException
	{
		if( playMode==null )
		{
			throw new NullArgumentException("Wrong argument playMode==null");
		}

		_waitingForm=new WaitingForGameFrame();
		_waitingForm.setVisible(true);
		_playMode=playMode;
	}
	
	
	/* (non-Javadoc)
	 * @see org.promasi.multiplayer.server.clientstate.IClientState#onReceive(org.promasi.multiplayer.ProMaSiClient, java.lang.String)
	 */
	@Override
	public void onReceive(ProMaSiClient client, String recData)throws NullArgumentException {
		if(recData==null)
		{
			throw new NullArgumentException("Wrong argument recData==null");
		}
		
		if(client==null)
		{
			throw new NullArgumentException("Wrong argument client==null");
		}
		
		try
		{
			Object object=RequestBuilder.buildRequest(recData);
			if(object instanceof StartGameRequest)
			{	
				StartGameRequest request=(StartGameRequest)object;
				Company company = request.getCompany();
		        company.setProjectManager( _playMode.getProjectManager());
		        _playMode.getShell().setCompany( company );
		        _playMode.setMarketPlace(request.getMarketPlace());
  
		        ICommunicator communicator=new Communicator();
		        communicator.setMainReceiver( _playMode.getShell().getModelMessageReceiver());
		            	
		        _playMode.getShell().setCurrentPlayMode(_playMode);
				changeClientState( client, new PlayingGameClientState(_playMode.getShell()) );
				_waitingForm.setVisible(false);
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
		} catch (ConfigurationException e) {
			client.sendMessage(new InternalErrorResponse().toProtocolString());
			client.disconnect();
		}
	}

}
