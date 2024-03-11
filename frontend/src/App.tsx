import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { IndexPage } from './containers/IndexPage.tsx';
import { useEffect } from 'react';
import { LoginPage } from './containers/LoginPage/LoginPage.tsx';
import { Menu } from './components/menu/Menubar.tsx';

function setScreenSize() {
	const vh = window.innerHeight * 0.01;
	document.documentElement.style.setProperty('--vh', `${vh}px`);
}

export default function App() {
	useEffect(() => {
		setScreenSize();

		const handleResize = () => {
			setScreenSize();
		};

		window.addEventListener('resize', handleResize);

		return () => {
			window.removeEventListener('resize', handleResize);
		};
	}, []);

	return (
		<>
			<BrowserRouter>
				<Routes>
					<Route>
						{/* 레이아웃이 필요한 페이지 */}
						<Route path='/' element={<IndexPage />} />
					</Route>
					<Route></Route>
				</Routes>
			</BrowserRouter>
		</>
	);
}
