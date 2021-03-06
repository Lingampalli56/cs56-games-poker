package edu.ucsb.cs56.projects.games.poker;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.ArrayList;


/**
	Class that represents a Texas Holdem' Style Poker Game.
*/

public class PokerGame {

    static int dealerWins = 0;
    static int playerWins = 0;
    static int totalGames = 0;

	private JPanel panel;
	private JFrame mainFrame,mainFrame2;
	private JFrame playButtonFrame;
	private JButton playButton, playAgainButton, turnButton, riverButton, foldButton;
	private JLabel winnerLabel;
	private JPanel dealerPanel, playerPanel, centerPanel, revealPanel;
	private Hand dealerHand, playerHand, flop;
	private Deck deck;
	private Card turnCard, riverCard, backCard;
	private ImageIcon backCardImage;
	
/**
	No arg constructor that initializes a new deck.
*/
	public PokerGame(){
		//deck=new Deck();
	}
	
/**
	Main method of PokerGame class.
*/
	public static void main(String[] args)
	{
		PokerGame gui=new PokerGame();
		gui.go();
	}
	
/**
	Creates a window with a Play button.
*/
	public void go(){
		
		playButtonFrame = new JFrame();
		playButtonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	
		
		panel=new JPanel();
		
		playButton=new JButton("Click to Play");
		playButton.addActionListener(new playButtonListener());
		panel.add(playButton,BorderLayout.CENTER);
		panel.setBackground(Color.darkGray);
		
		playButtonFrame.add(BorderLayout.CENTER, panel);
		playButtonFrame.setSize(200,200);
		
		playButtonFrame.setVisible(true);
	}
	
/**
	Sets up the player's and dealer's hand.
*/	
	public void playerSetUp(){
		deck=new Deck();

        // Deal player hands
		playerHand=new Hand(deck);
		dealerHand=new Hand(deck);

        // Burn card before dealing flop
        deck.burnCard();
		flop=deck.showFlop();

        // Prepare turn and river cards
        deck.burnCard();
		turnCard=deck.returnCard();

        deck.burnCard();
		riverCard=deck.returnCard();
		backCard=new Card(100,"B");

		for(Card c: flop){
			playerHand.add(c);
			dealerHand.add(c);
		}

        // Add cards to dealer/ player hands to find winner
		playerHand.add(turnCard);
		playerHand.add(riverCard);

		dealerHand.add(turnCard);
		dealerHand.add(riverCard);
		
		String dir="Cards/";
		String cardFile="B.png";
		URL url=getClass().getResource(dir+cardFile);
		backCardImage=new ImageIcon(url);
		
	}
	
/**
	Returns an ImageIcon by using the URL class in order to make the 
	ImageIcon web compatible.
	@param c card whose image is to be retrieved.
*/
	public ImageIcon getCardImage(Card c){
		String dir="Cards/";
		String cardFile=c.toString()+".png";
		URL url=getClass().getResource(dir+cardFile);
		return new ImageIcon(url);
		
	}

    /**
     * Lay out game view
     */

    private void layoutView()
    {
        dealerPanel=new JPanel();
        playerPanel=new JPanel();
        centerPanel=new JPanel();
        revealPanel=new JPanel();
        turnButton=new JButton("TURN");
        foldButton = new JButton("FOLD");
        turnButton.addActionListener(new turnButtonListener());
        foldButton.addActionListener(new foldButtonListener());
        for(int i=0;i<2;i++){
            dealerPanel.add(new JLabel(backCardImage));
            playerPanel.add(new JLabel(getCardImage(playerHand.get(i))));
        }
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        for(int i=0;i<3;i++){
            centerPanel.add(BorderLayout.CENTER, new
                    JLabel(getCardImage(flop.get(i))));
        }
        dealerPanel.add(new JLabel("DEALER"));
        playerPanel.add(new JLabel("PLAYER"));
        playerPanel.add(foldButton);
        centerPanel.add(BorderLayout.SOUTH,turnButton);

        if(playerHand.compareHands(dealerHand)==1){
            winnerLabel=new JLabel("YOU WON!");
        }
        else if(playerHand.compareHands(dealerHand)==2){
            winnerLabel=new JLabel("TIED");
        }
        else{
            winnerLabel=new JLabel("Dealer won");
        }

        deck.reShuffle();

        /**dealerPanel.add(new JLabel(Integer.toString(dealerHand.getHandValue())));
         playerPanel.add(new JLabel(Integer.toString(playerHand.getHandValue())));
         for(int i:dealerHand.sortHand()){
         dealerPanel.add(new JLabel(Integer.toString(i)));
         }
         for(int i:playerHand.sortHand()){
         playerPanel.add(new JLabel(Integer.toString(i)));
         }*/

        mainFrame=new JFrame();
        mainFrame.setSize(600,600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().add(BorderLayout.NORTH, dealerPanel);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, playerPanel);
        mainFrame.getContentPane().add(BorderLayout.CENTER, centerPanel);
    }
	
/**
	Method that sets up a new Poker game with new hands.
*/
	public void replay(){
		playerSetUp();
  		
        layoutView();
  		mainFrame.setVisible(true);
  	
	}

/**
	Sets up the Poker game when the client clicks the Play button.
*/
	class playButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
	
  			playerSetUp();
  		
            layoutView();
  			playButtonFrame.dispose();
  			mainFrame.setVisible(true);
  	}
  }
/**
	Displays the turn card when the user clicks the button.
*/
  class turnButtonListener implements ActionListener{
  	public void actionPerformed(ActionEvent event){
  		centerPanel.remove(turnButton);
  		centerPanel.add(new JLabel(getCardImage(turnCard)));
  		riverButton=new JButton("RIVER");
  		riverButton.addActionListener(new riverButtonListener());
  		centerPanel.add(riverButton);
  		mainFrame.setVisible(false);
  		mainFrame.setVisible(true);
  		}
  }

    /**
     * Restarts game
     */

    class foldButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            dealerWins += 1;
            replay();
        }
    }

/**
	Displays the river card when the user clicks the button.
*/  
  class riverButtonListener implements ActionListener{
  	public void actionPerformed(ActionEvent event){
  		centerPanel.remove(riverButton);
  		centerPanel.add(new
  		JLabel(getCardImage(riverCard)));
  		
  		playAgainButton=new JButton("Play Again");
  		playAgainButton.addActionListener(new playAgainListener());
  	
  		deck.reShuffle();	
  		centerPanel.add(winnerLabel);
  		centerPanel.add(playAgainButton);
  		
  		for(int i=0;i<2;i++)
  			revealPanel.add(new JLabel(getCardImage(dealerHand.get(i))));
  		revealPanel.add(new JLabel("DEALER"));

  		mainFrame.remove(dealerPanel);
  		mainFrame.add(revealPanel);
  		mainFrame.setVisible(false);
  		mainFrame.setVisible(true);
  	}
  }
  
/**
	Sets up the Poker game when the Client clicks the PlayAgain button.
*/
  class playAgainListener implements ActionListener{
  	public void actionPerformed(ActionEvent event){
  		mainFrame.dispose();
  		PokerGame gui2=new PokerGame();
  		gui2.replay();
  	}
  	
  }



}
















