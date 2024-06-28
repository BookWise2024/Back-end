import { useState, useEffect } from 'react'
import { BrowserRouter, Route, Routes, Link } from 'react-router-dom'
import axios from 'axios';
import './App.css'
// import './App.module.css'
import Login from './Login/Login.jsx';
import MainPage from './MainPage/MainPage.jsx';
import Callback from './Login/Callback.jsx';
import SubMenu from './HambergerMenu/SubMenu.jsx';
import Review from './Review/Review.jsx';

function App() {
  const [count, setCount] = useState(0)

  // const [member, setMember] = useState('');

  // useEffect(() => {
  //   axios.get('/api/login')
  //       .then(response => setMember(response.data))
  //       .catch(error => console.log(error))
  // }, []);

  return (
    <>
      <ul>
        <li><Link to = "/login">Kakao Login</Link></li>
        <li><Link to = "/">Main Page</Link></li>
        <li><Link to = "/sub">Hamberger Menu</Link></li>
        <li><Link to = "/review">Review Page</Link></li>
      </ul>
      <Routes>
        <Route path = "/login" element={<Login />}/>
        <Route path = "/auth/kakao/callback" element={<Callback />}/>
        <Route path = "/" element={<MainPage />}/>
        <Route path = "/sub" element={<SubMenu />}/>
        <Route path = "/review" element={<Review />}/>
      </Routes>

      {/* spring boot 연결 테스트 코드 */}
      {/* <div>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div> */}
      <h1>Vite + React {/* {member} */} </h1>
      {/* <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p> */}
    </>
  )
}

export default App
