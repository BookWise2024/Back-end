import { useState } from 'react'
import kakao from './Kakao.module.css'
import layout from '../CommonStyles/Layout.module.css'

export default function Login() {

    const Rest_api_key='451aea6cf5b0142c652610b32748b4e9' //REST API KEY
    const redirect_uri = 'http://localhost:5173/auth/kakao/callback' //Redirect URI
    // oauth 요청 URL
    const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${Rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`
    const handleLogin = ()=>{
        window.location.href = kakaoURL
    }

    return (
        <div className={ layout.layout }>
            <div className={ kakao.titleBase }>
                <div className={ kakao.subTitle }>AI 기반 도서 추천 서비스</div>
                <div className={ kakao.mainTitle }>로고</div>
            </div>
            <div className={ kakao.desc }>
                간편하게 로그인하고 다양한 서비스를 이용해보세요
            </div>
            <button onClick={handleLogin} className={kakao.kakaoButton}>
                {/* image는 kakao.module.css에서 백그라운드로 설정 */}
                <div className={kakao.buttonContent}>
                    <div className={kakao.kakaoImage}></div>
                    <div className={kakao.buttonText}>
                        카카오로 1초 만에 시작하기
                    </div>
                </div>
            </button>
        </div>
    )
}