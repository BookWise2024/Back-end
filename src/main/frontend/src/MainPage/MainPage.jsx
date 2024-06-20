import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Home = () => {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post('http://localhost:8080/logout', {}, { withCredentials: true });
            navigate('/login');
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    return (
        <div>
            <h1>Home</h1>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
};

export default Home;
