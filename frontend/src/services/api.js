import axios from 'axios';

// Base URL — uses React's proxy (package.json) in dev, or set env var in prod
const BASE_URL = process.env.REACT_APP_API_URL || '';

const api = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 90000, // 90s — HF free tier can be slow on cold start
});

/**
 * Send a question to the LLM via Spring Boot backend.
 * @param {string} question
 * @param {string} [context]
 * @returns {Promise<{answer, model, responseTimeMs, timestamp}>}
 */
export const askQuestion = async (question, context = '') => {
  const response = await api.post('/api/ask', { question, context });
  return response.data;
};

/**
 * Check if the backend is up.
 * @returns {Promise<{status, app, model}>}
 */
export const healthCheck = async () => {
  const response = await api.get('/api/health');
  return response.data;
};
