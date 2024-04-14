import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Card from '@mui/material/Card';
import {Box, IconButton, styled, tableCellClasses, Typography} from "@mui/material";
import Collapse from '@mui/material/Collapse';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import {useState} from "react";

function createData(product) {
    return {
        name: product.name,
        category: product.category.name,
        characteristics: product.characteristics
    }
}

const columns = [
    { field: 'name', headerName: 'Product name', width: 10 },
    { field: 'category', headerName: 'Category', width: 10 }
];

const products = [
    {
        "id": 2,
        "category": {
            "id": 4,
            "name": "alcohol"
        },
        "name": "pivo",
        "characteristics": "hmilne, smachne"
    }
]


const rows = [
    createData(products[0])
];

const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
        backgroundColor: '#748c8d',
        color: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
        fontSize: 14,
    },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
    '&:nth-of-type(odd)': {
        backgroundColor: theme.palette.action.hover,
    },
    '&:last-child td, &:last-child th': {
        border: 0,
    },
}));

function Row(props) {
    const { row } = props;
    const [open, setOpen] = useState(false);

    return (
        <React.Fragment>
            <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
                <StyledTableCell>
                    <IconButton
                        aria-label="expand row"
                        size="small"
                        onClick={() => setOpen(!open)}
                    >
                        {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
                    </IconButton>
                </StyledTableCell>
                <StyledTableCell component="th" scope="row "align="left">{row.name}</StyledTableCell>
                <StyledTableCell align="left">{row.category}</StyledTableCell>
            </TableRow>
            <TableRow>
                <StyledTableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={3}>
                    <Collapse in={open} timeout="auto" unmountOnExit>
                        <Box sx={{ margin: 1 }}>
                            <Typography variant="h6" gutterBottom component="div">
                                Characteristics
                            </Typography>
                            {row.characteristics}
                        </Box>
                    </Collapse>
                </StyledTableCell>
            </TableRow>
        </React.Fragment>
    );
}

export default function Products() {
    return (
        <TableContainer component={Card} sx={{ maxWidth: 700, margin: '30px auto' }}>
            <Table aria-label="collapsible table">
                <TableHead>
                    <StyledTableRow>
                        <StyledTableCell/>
                        {columns.map((column) => (
                            <StyledTableCell key={column.field}>
                                {column.headerName}
                            </StyledTableCell>
                        ))}
                    </StyledTableRow>
                </TableHead>
                <TableBody>
                    {rows.map((row) => (
                        <Row key={row.name} row={row} />
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
        // <TableContainer component={Card} sx={{ maxWidth: 700, margin: '30px auto' }}>
        //     <Table sx={{ minWidth: 650 }} aria-label="simple table">
        //         <TableHead>
        //             <StyledTableRow>
        //                 {columns.map((column) => (
        //                     <StyledTableCell key={column.field}>
        //                         {column.headerName}
        //                     </StyledTableCell>
        //                 ))}
        //             </StyledTableRow>
        //         </TableHead>
        //         <TableBody>
        //             {rows.map((row) => (
        //                 <StyledTableRow key={row.name}>
        //                     {columns.map((column) => (
        //                         <StyledTableCell key={column.field} align="left">
        //                             {row[column.field]}
        //                         </StyledTableCell>
        //                     ))}
        //                 </StyledTableRow>
        //             ))}
        //         </TableBody>
        //     </Table>
        // </TableContainer>
    );
}