import React from 'react';
import './TypingIndicator.css';

function TypingIndicator() {
  return (
    <div className="typing-row">
      <div className="avatar avatar-bot">AI</div>
      <div className="typing-bubble">
        <span className="dot" style={{ animationDelay: '0ms' }} />
        <span className="dot" style={{ animationDelay: '160ms' }} />
        <span className="dot" style={{ animationDelay: '320ms' }} />
      </div>
    </div>
  );
}

export default TypingIndicator;
