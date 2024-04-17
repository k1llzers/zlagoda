import {styled, tableCellClasses} from "@mui/material";
import TableCell from "@mui/material/TableCell";

const StyledTableCell = styled(TableCell)(({theme}) => ({
    [`&.${tableCellClasses.head}`]: {
        backgroundColor: '#748c8d',
        color: theme.palette.common.white,
        fontFamily: 'Segoe UI',
        fontSize: 18,
        fontWeight: 'normal',
    },
    [`&.${tableCellClasses.body}`]: {
        fontFamily: 'Segoe UI',
        fontSize: 16
    },
}));

export default StyledTableCell;