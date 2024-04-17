import {Select, styled} from "@mui/material";

const StyledSelect = styled(Select)(({ theme }) => ({
    '& .MuiSelect-select': {
        color: '#34383b',
        '&:focus': {
            backgroundColor: 'transparent',
        },
    },
    '& .MuiOutlinedInput-notchedOutline': {
        '& fieldset': {
            borderColor: '#E0E3E7',
        },
        '&:hover fieldset': {
            borderColor: '#B2BAC2',
        },
        '&.Mui-focused fieldset': {
            borderColor: '#6F7E8C',
        },
    },

    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
        borderColor: '#6F7E8C',
    },
    margin: '10px',
}));

export default StyledSelect;