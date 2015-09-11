/**
 * 
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
	
	public LogView()
	{
		m_dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		createView();
	}
	
	public void createView()
	{
		m_frame = new JFrame("Log View");
		m_frame.setVisible(false);
		m_frame.setSize(750, 400);
		m_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		m_frame.getContentPane().setLayout(new BorderLayout());
		
		m_textArea.setEditable(false);
		m_textArea.setLineWrap(m_lineWrap);
		
		m_scrollPane = new JScrollPane(m_textArea);
		m_scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		m_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		m_frame.getContentPane().add(m_scrollPane, BorderLayout.CENTER);
		
		
		
		JPanel controlPanel = new JPanel();
		m_clearButton = new JButton("Clear");
		m_clearButton.addActionListener(this);
		m_clearButton.setEnabled(true);
		
		m_autoScrollCheckBox = new JCheckBox("AutoScroll", m_autoScroll);
		m_autoScrollCheckBox.addItemListener(this);
		m_autoScrollCheckBox.setEnabled(true);
		
		m_lineWrapCheckBox = new JCheckBox("LineWrap", m_lineWrap);
		m_lineWrapCheckBox.addItemListener(this);
		m_lineWrapCheckBox.setEnabled(true);
		
		m_loggingEnabledCheckBox = new JCheckBox("LoggingEnabled", m_loggingEnabled);
		m_loggingEnabledCheckBox.addItemListener(this);
		m_loggingEnabledCheckBox.setEnabled(true);
		
		controlPanel.add(m_clearButton);
		controlPanel.add(m_autoScrollCheckBox);
		controlPanel.add(m_lineWrapCheckBox);
		controlPanel.add(m_loggingEnabledCheckBox);
		
		m_frame.add(controlPanel, BorderLayout.NORTH);
		
		m_frame.setVisible(true);
	}
	
	public void setVisible(boolean visible)
	{
		m_frame.setVisible(visible);
	}
	
	public void setSize(Dimension dimension)
	{
		if (null != dimension)
		{
			m_frame.setSize(dimension);
		}
	}
	
	public void setSize(int width, int height)
	{
		m_frame.setSize(width, height);
	}
	
	public void setLineWrap(boolean lineWrap)
	{
		m_textArea.setLineWrap(lineWrap);
		m_lineWrap = lineWrap;
	}
	
	public void setAutoScroll(boolean autoScroll)
	{
		m_autoScrollCheckBox.setSelected(autoScroll);
		m_autoScroll = autoScroll;
	}
	
	public void setLoggingEnabled(boolean loggingEnabled)
	{
		m_loggingEnabledCheckBox.setSelected(loggingEnabled);
		m_loggingEnabled = loggingEnabled;
	}
	
	public void clearText()
	{
		m_textArea.setText("");
	}
	
	public String getText()
	{
		return m_textArea.getText();
	}
	
	public void handleCommmunicationText(String text)
	{
		appendText(text);
	}
	
	public void handleErrorMessage(String text)
	{
		appendText(text);
	}
	
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
	
	private String getDateStr()
	{
		return m_dateFormat.format(new Date());
	}

	public void itemStateChanged(ItemEvent event)
	{
		if (event.getSource() == m_autoScrollCheckBox)
		{
			JCheckBox checkBox = (JCheckBox) event.getSource();
			if (null != checkBox)
			{
				setAutoScroll(checkBox.isSelected());
			}
		}
		else if (event.getSource() == m_lineWrapCheckBox)
		{
			JCheckBox checkBox = (JCheckBox) event.getSource();
			if (null != checkBox)
			{
				setLineWrap(checkBox.isSelected());
			}
		}
		else if (event.getSource() == m_loggingEnabledCheckBox)
		{
			JCheckBox checkBox = (JCheckBox) event.getSource();
			if (null != checkBox)
			{
				setLoggingEnabled(checkBox.isSelected());
			}
		}
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == m_clearButton)
		{
			clearText();
		}
	}

}
