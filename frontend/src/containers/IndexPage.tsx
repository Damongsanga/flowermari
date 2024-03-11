import { Menu } from '../components/menu/Menubar';
import { StyledIndexPage, StyledBox, StyledInput, StyledLetter, StyledTextarea, StyledText, StyleImage } from './StyledIndexPage';
import mainFlower from '../assets/images/mainFlower.png';
import { ChangeEvent, useState } from 'react';

export const IndexPage = () => {
	const [value1, setValue1] = useState<string>('');
	const [value2, setValue2] = useState<string>('');

	const handleChangeOptionValues = (e: ChangeEvent<HTMLTextAreaElement>, setValue: (value: string) => void) => {
		const { value } = e.target;

		if (e.target.scrollHeight === e.target.clientHeight) {
			setValue(value);
		}
	};

	return (
		<>
			<Menu link='https://lh3.googleusercontent.com/proxy/h5DzWTZmrGsdyQ2U7EmbvpPv3UXRxFSC_nGqk_7nhcQ_nR_yuco7jHQwNj0ZE2KFaGnto2PPeIuVIO6Hhf4DTl0_qwY-iAHLHN0tJdYMmFMtt2fg0kN4CYx9jaaCEWk'></Menu>
			<StyledIndexPage>
				<StyledBox>
					<StyleImage src={mainFlower}></StyleImage>
					<StyledText>꽃을 통해 전하고 싶은 말을 전해보세요. </StyledText>
					<StyledText>전하고 싶은 말을 적으면 꽃말을 기준으로 </StyledText>
					<StyledText>꽃을 선택해 꽃다발을 만들어드려요. </StyledText>

					<StyledLetter>
						<StyledText>상황</StyledText>
						<StyledInput placeholder='예) 여자친구와 200일'></StyledInput>
						<StyledTextarea
							value={value1}
							placeholder='dear : 귀엽고 사랑스러운 여자친구에게'
							onChange={(e) => handleChangeOptionValues(e, setValue1)}
						></StyledTextarea>
						<StyledTextarea
							value={value2}
							placeholder='꽃을 통해 전하고 싶은 마음을 적어주세요.'
							height='88px'
							onChange={(e) => handleChangeOptionValues(e, setValue2)}
						></StyledTextarea>
					</StyledLetter>
				</StyledBox>
			</StyledIndexPage>
		</>
	);
};
