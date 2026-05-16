const BASE = '/api'

function getToken() {
  return localStorage.getItem('token')
}

async function request(method, path, body, isForm = false) {
  const headers = {}
  const token = getToken()
  
  if (token) headers['Authorization'] = `Bearer ${token}`
  if (body && !isForm) headers['Content-Type'] = 'application/json'

  const res = await fetch(`${BASE}${path}`, {
    method,
    headers,
    body: isForm ? body : body ? JSON.stringify(body) : undefined,
  })

  const data = await res.json().catch(() => ({}))
  if (!res.ok) throw new Error(data.error || `Error ${res.status}`)
  return data
}

export const api = {
  get: (path) => request('GET', path),
  post: (path, body) => request('POST', path, body),
  postForm: (path, formData) => request('POST', path, formData, true),

  login: (username, password) => request('POST', '/auth/login', { username, password }),
}
