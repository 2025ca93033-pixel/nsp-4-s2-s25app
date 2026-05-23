import React, { useState, useRef, useEffect } from 'react';
import ChatMessage from './components/ChatMessage';
import TypingIndicator from './components/TypingIndicator';
import { askQuestion, healthCheck } from './services/api';
import './App.css';

const WELCOME_MESSAGE = {
  role: 'assistant',
  text: "Hello! I'm your AI assistant powered by Hugging Face LLM via a Spring Boot backend. Ask me anything — questions, explanations, or just chat.",
};

const QUICK_PROMPTS = [
  'What is DevOps?',
  'Explain CI/CD pipeline',
  'What is Docker?',
  'Difference between Git merge and rebase?',
];

function App() {
  const [messages, setMessages] = useState([WELCOME_MESSAGE]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState(null); // backend health
  const bottomRef = useRef(null);
  const inputRef = useRef(null);

  // Check backend health on mount
  useEffect(() => {
    healthCheck()
      .then(data => setStatus({ online: true, model: data.model }))
      .catch(() => setStatus({ online: false }));
  }, []);

  // Auto-scroll to latest message
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, loading]);

  const sendMessage = async (text) => {
    const question = (text || input).trim();
    if (!question || loading) return;

    setInput('');
    setMessages(prev => [...prev, { role: 'user', text: question }]);
    setLoading(true);

    try {
      const data = await askQuestion(question);
      setMessages(prev => [
        ...prev,
        {
          role: 'assistant',
          text: data.success ? data.answer : (data.errorMessage || 'Something went wrong.'),
          responseTimeMs: data.responseTimeMs,
          error: !data.success,
        },
      ]);
    } catch (err) {
      setMessages(prev => [
        ...prev,
        {
          role: 'assistant',
          text: 'Could not reach the backend. Make sure Spring Boot is running on port 8080.',
          error: true,
        },
      ]);
    } finally {
      setLoading(false);
      inputRef.current?.focus();
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };

  const clearChat = () => {
    setMessages([WELCOME_MESSAGE]);
  };

  return (
    <div className="app">
      {/* ── Header ── */}
      <header className="header">
        <div className="header-left">
          <span className="logo-badge">NSP</span>
          <div>
            <h1 className="header-title">NSP-4-S2-S25App</h1>
            <p className="header-sub">AI Q&amp;A · Spring Boot + React · Hugging Face LLM</p>
          </div>
        </div>
        <div className="header-right">
          <span className={`status-dot ${status?.online ? 'online' : 'offline'}`} />
          <span className="status-text">
            {status === null ? 'Checking…' : status.online ? `Backend UP · ${status.model}` : 'Backend offline'}
          </span>
          <button className="clear-btn" onClick={clearChat} title="Clear chat">
            ↺
          </button>
        </div>
      </header>

      {/* ── Chat Area ── */}
      <main className="chat-area">
        {messages.map((msg, i) => (
          <ChatMessage key={i} {...msg} />
        ))}
        {loading && <TypingIndicator />}
        <div ref={bottomRef} />
      </main>

      {/* ── Quick Prompts ── */}
      {messages.length <= 1 && (
        <div className="quick-prompts">
          {QUICK_PROMPTS.map(q => (
            <button key={q} className="quick-btn" onClick={() => sendMessage(q)}>
              {q}
            </button>
          ))}
        </div>
      )}

      {/* ── Input Bar ── */}
      <footer className="input-bar">
        <textarea
          ref={inputRef}
          className="input-field"
          rows={1}
          placeholder="Ask anything… (Enter to send, Shift+Enter for newline)"
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          disabled={loading}
        />
        <button
          className="send-btn"
          onClick={() => sendMessage()}
          disabled={loading || !input.trim()}
        >
          {loading ? '…' : '↑'}
        </button>
      </footer>
    </div>
  );
}

export default App;
