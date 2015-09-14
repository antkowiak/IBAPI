/**
 * LogView - Create and display a log window.  Implements the interface
 * to listen for IB communication and IB error messages.
 */
package com.ryanantkowiak.IBAPI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class LogView implements IBCommunicationListener, IBErrorMessageListener, ActionListener, ItemListener
{
	private JFrame m_frame;
	private JScrollPane m_scrollPane;
	private JTextArea m_textArea = new JTextArea();
	
	private JButton m_clearButton;
	private JCheckBox m_autoScrollCheckBox;
	private JCheckBox m_lineWrapCheckBox;
	private JCheckBox m_loggingEnabledCheckBox;
	
	private SimpleDateFormat m_dateFormat;
	private boolean m_autoScroll = true;
	private boolean m_lineWrap = true;
	private boolean m_loggingEnabled = true;
	
	/*
	 * @brief	Constructor
	 */
	public LogView()
	{
		m_dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		createView();
	}
	
	/*
	 * @brief	Creates the on-screen view objects
	 */
	private void createView()
	{
		// Set up the outter frame
		m_frame = new JFrame("Log View");
		m_frame.setVisible(false);
		m_frame.setSize(750, 400);
		m_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		m_frame.getContentPane().setLayout(new BorderLayout());
		
		// Set up the text area
		m_textArea.setEditable(false);
		m_textArea.setLineWrap(m_lineWrap);
		
		// Set up the scroll pane
		m_scrollPane = new JScrollPane(m_textArea);
		m_scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		m_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		m_frame.getContentPane().add(m_scrollPane, BorderLayout.CENTER);
		
		// Set up the panel for controls
		JPanel controlPanel = new JPanel();
		
		// Create a button to clear the log
		m_clearButton = new JButton("Clear");
		m_clearButton.addActionListener(this);
		m_clearButton.setEnabled(true);
		
		// Create a checkbox to enable/disable autoscrolling
		m_autoScrollCheckBox = new JCheckBox("AutoScroll", m_autoScroll);
		m_autoScrollCheckBox.addItemListener(this);
		m_autoScrollCheckBox.setEnabled(true);
		
		// Create a checkbox to enable/disable line wrapping
		m_lineWrapCheckBox = new JCheckBox("LineWrap", m_lineWrap);
		m_lineWrapCheckBox.addItemListener(this);
		m_lineWrapCheckBox.setEnabled(true);
		
		// Create a checkbox to enable/disable logging
		m_loggingEnabledCheckBox = new JCheckBox("LoggingEnabled", m_loggingEnabled);
		m_loggingEnabledCheckBox.addItemListener(this);
		m_loggingEnabledCheckBox.setEnabled(true);
		
		// Add the controls
		controlPanel.add(m_clearButton);
		controlPanel.add(m_autoScrollCheckBox);
		controlPanel.add(m_lineWrapCheckBox);
		controlPanel.add(m_loggingEnabledCheckBox);
		
		m_frame.add(controlPanel, BorderLayout.NORTH);
		m_frame.setVisible(true);
	}
	
	/*
	 * @brief	Set the LogView window's visibility
	 * @param	visible - true if visible
	 */
	public void setVisible(boolean visible)
	{
		m_frame.setVisible(visible);
	}
	
	/*
	 *  @brief	Set the LogView window's size
	 *  @param	dimension - The size dimension to set the window to
	 */
	public void setSize(Dimension dimension)
	{
		if (null != dimension)
		{
			m_frame.setSize(dimension);
		}
	}
	
	/*
	 *  @brief	Set the LogView window's size
	 *  @param	width - The width of the window in pixels
	 *  @param	height - The height of the window in pixels
	 */
	public void setSize(int width, int height)
	{
		m_frame.setSize(width, height);
	}
	
	/*
	 * @brief	Set the option for wrapping lines
	 * @param	lineWrap - true if line wrapping is enabled
	 */
	public void setLineWrap(boolean lineWrap)
	{
		m_textArea.setLineWrap(lineWrap);
		m_lineWrap = lineWrap;
	}
	
	/*
	 * @brief	Set the option for automatic scrolling
	 * @brief	autoScroll - true if auto scrolling is enabled
	 */
	public void setAutoScroll(boolean autoScroll)
	{
		m_autoScrollCheckBox.setSelected(autoScroll);
		m_autoScroll = autoScroll;
	}
	
	/*
	 * @brief	Set the option for enabling/disabling logging
	 * @brief	loggingEnabled - true if logging is enabled
	 */
	public void setLoggingEnabled(boolean loggingEnabled)
	{
		m_loggingEnabledCheckBox.setSelected(loggingEnabled);
		m_loggingEnabled = loggingEnabled;
	}
	
	/*
	 * @brief	Clears the text in the log window
	 */
	public void clearText()
	{
		m_textArea.setText("");
	}
	
	/*
	 * @brief	Returns the text of the log window
	 * @return	String - the text of the log window
	 */
	public String getText()
	{
		return m_textArea.getText();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ryanantkowiak.IBAPI.IBCommunicationListener#handleCommmunicationText(java.lang.String)
	 * 
	 * @brief	Handles communicationText from IB API
	 * @param	text - the communication text from IB API
	 */
	public void handleCommmunicationText(String text)
	{
		appendText(text);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ryanantkowiak.IBAPI.IBErrorMessageListener#handleErrorMessage(java.lang.String)
	 * 
	 * @brief	Handles error message text from IB API
	 * @param	text - the error text from IB API
	 */
	public void handleErrorMessage(String text)
	{
		appendText(text);
	}
	
	/*
	 * @brief	Appends text to the log window
	 * @param	text - the text to append to the log window
	 */
	private void appendText(String text)
	{
		if (text != null && m_loggingEnabled)
		{
			text = text.replaceFirst("\\s+$", "");
			if (!text.isEmpty())
			{
				m_textArea.append(getDateStr() + " - " + text + "\n");
				
				if (m_autoScroll)
				{
					m_textArea.setCaretPosition(m_textArea.getText().length());
				}
			}
		}
	}
	
	/*
	 * @brief	Return a formatted date string
	 * @return	String - The formatted date string
	 */
	private String getDateStr()
	{
		return m_dateFormat.format(new Date());
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 * 
	 * @brief	Handle the state changes of some checkboxes for options/settings
	 * @param	event - the event object that had a state change
	 */
	public void itemStateChanged(ItemEvent event)
	{
		// Enable/Disable the autoscroll
		if (event.getSource() == m_autoScrollCheckBox)
		{
			JCheckBox checkBox = (JCheckBox) event.getSource();
			if (null != checkBox)
			{
				setAutoScroll(checkBox.isSelected());
			}
		}
		// Enable/Disable the line wrapping
		else if (event.getSource() == m_lineWrapCheckBox)
		{
			JCheckBox checkBox = (JCheckBox) event.getSource();
			if (null != checkBox)
			{
				setLineWrap(checkBox.isSelected());
			}
		}
		// Enable/Disable the logging
		else if (event.getSource() == m_loggingEnabledCheckBox)
		{
			JCheckBox checkBox = (JCheckBox) event.getSource();
			if (null != checkBox)
			{
				setLoggingEnabled(checkBox.isSelected());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * @brief	Handle mouse clicks on buttons
	 * @param	event - the event object that had an action performed
	 */
	public void actionPerformed(ActionEvent event)
	{
		// Handle the "Clear" button click
		if (event.getSource() == m_clearButton)
		{
			clearText();
		}
	}

}
