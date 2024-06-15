import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';

const Callback = () => {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            const params = new URLSearchParams(location.search);
            // 인가 코드
            const code = params.get('code');

            if (code) {
                try {
                    const response = await axios.post('http://localhost:8080/api/auth/kakao/callback', { code });
                    console.log(response.data); // 성공적으로 받은 사용자 데이터
                    navigate('/kakao'); // 처리 후 원하는 경로로 이동
                } catch (error) {
                    console.error('Error during the callback processing', error);
                }
            }
        };

        fetchData();
    }, [location, navigate]);

    return (
        <div>
            <h1>Processing login...</h1>
        </div>
    );
};

export default Callback;
