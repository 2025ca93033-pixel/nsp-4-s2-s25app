import React from 'react';
import './ChatMessage.css';

/**
 * Renders a single chat bubble.
 * @param {{role: 'user'|'assistant', text: string, responseTimeMs?: number, error?: boolean}} props
 */
function ChatMessage({ role, text, responseTimeMs, error }) {
  const isUser = role === 'user';

  return (
    <div className={`message-row ${isUser ? 'user-row' : 'assistant-row'}`}>
      <div className={`avatar ${isUser ? 'avatar-user' : 'avatar-bot'}`}>
        {isUser ? 'U' : 'AI'}
      </div>
      <div className={`bubble ${isUser ? 'bubble-user' : error ? 'bubble-error' : 'bubble-bot'}`}>
        <p className="bubble-text">{text}</p>
        {!isUser && responseTimeMs && (
          <span className="meta">{(responseTimeMs / 1000).toFixed(1)}s</span>
        )}
      </div>
    </div>
  );
}

export default ChatMessage;
