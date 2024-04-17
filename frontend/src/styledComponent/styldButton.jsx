import {Button, styled} from "@mui/material";

const StyledButton = styled(Button)({
    borderColor: "#748c8d",
    color: "#748c8d",
    '&:hover': {
        borderColor: "#2d434b",
        color: "#2d434b",
    }
})

export default StyledButton;