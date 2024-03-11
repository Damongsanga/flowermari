import styled from 'styled-components';

interface HeightProps {
	height?: string;
}

export const StyledIndexPage = styled.div`
	height: calc(100vh - 70px);
	position: relative;

	display: flex;
	justify-content: center;
	align-items: center;
`;

export const StyledBox = styled.div`
	width: 85vw;
	height: 75vh;

	border-radius: 15px;
	padding: 5px;

	display: flex;
	justify-content: center;
	align-items: center;

	background-color: #e2d5e7;
	flex-direction: column;
`;

export const StyledInput = styled.input`
	width: 70vw;
	height: 3vh;

	border-left-width: 0;
	border-right-width: 0;
	border-top-width: 0;
	border-bottom-width: 1.5px;
	border-bottom-color: #fff6f2;

	padding-bottom: -1px;

	background-color: transparent;

	line-height: 22px;

	::placeholder {
		color: #999;
		font-family: 'Inter', sans-serif;
		font-weight: 400;
	}

	&:focus {
		outline: none;
		border-bottom-width: 1.5px;
		border-bottom-color: #fff6f2;
	}
`;

export const StyledLetter = styled.div`
	width: 75vw;
	height: 40vh;

	border: 2px solid #fff6f2;
	border-radius: 15px;
  margin-top: 2vh;

	display: flex;
	justify-content: center;
	align-items: center;

	flex-direction: column;
`;

export const StyledTextarea = styled.textarea<HeightProps>`
	width: 70vw;
	height: ${(props) => props.height || '44px'};

	background-attachment: local;
	background-image: linear-gradient(to right, transparent 15px, transparent 15px),
		linear-gradient(to left, transparent 15px, transparent 15px),
		repeating-linear-gradient(transparent, transparent 20px, #fff6f2 21px, #fff6f2 22px, #fff6f2 10px);
	line-height: 22px;

	border: none;
	background-color: inherit;
	resize: none;
	overflow: hidden;

	word-break: break-all;
	overflow: hidden;

	&:focus {
		outline: none;
	}

	&::placeholder {
    font-size: 0.725rem;
		color: #999 bold;
		font-family: 'Inter', sans-serif;
		font-weight: 400;
	}
`;

export const StyledText = styled.span`
	font-size: 0.875rem;
  display: block;
`;

export const StyleImage = styled.img`
  width: 100px; 
  height: 140px; 
`;