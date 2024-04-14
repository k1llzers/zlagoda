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
import {useEffect, useState} from "react";
import axios from "axios";

const Products = () => {
    const [products, setProducts] = useState([])

    const fetchProductsData = async () => {
        try {
            console.log(axios.defaults.headers)
            const response = await axios.get("http://localhost:8080/api/product",
                {skipAuth:false})
            setProducts(response.data);
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        fetchProductsData();
    }, []);

    function createData(product) {
        return {
            name: product.name,
            category: product.category.name,
            characteristics: product.characteristics
        }
    }

    const columns = [
        {field: 'name', headerName: 'Product name', width: 10},
        {field: 'category', headerName: 'Category', width: 10}
    ];

    const rows = products.map((product) => createData(product));

    const StyledTableCell = styled(TableCell)(({theme}) => ({
        [`&.${tableCellClasses.head}`]: {
            backgroundColor: '#748c8d',
            color: theme.palette.common.white,
        },
        [`&.${tableCellClasses.body}`]: {
            fontSize: 16,
            fontFamily: 'Segoe UI'
        },
    }));

    const StyledTableRow = styled(TableRow)(({theme}) => ({
        '&:nth-of-type(odd)': {
            backgroundColor: theme.palette.action.hover,
        },
        '&:last-child td, &:last-child th': {
            border: 0,
        },
    }));

    function Row(props) {
        const {row} = props;
        const [open, setOpen] = useState(false);

        return (
            <React.Fragment>
                <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                    <StyledTableCell>
                        <IconButton
                            aria-label="expand row"
                            size="small"
                            onClick={() => setOpen(!open)}
                        >
                            {open ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}
                        </IconButton>
                    </StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="left">{row.name}</StyledTableCell>
                    <StyledTableCell align="left">{row.category}</StyledTableCell>
                </TableRow>
                <TableRow>
                    <StyledTableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={3}>
                        <Collapse in={open} timeout="auto" unmountOnExit>
                            <Box sx={{margin: 1}}>
                                <Typography variant="span" gutterBottom component="div" style={{fontSize: 18}}>
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

    return (
        <TableContainer component={Card} sx={{ maxWidth: 700, margin: '30px auto' }}>
            <Table aria-label="collapsible table">
                <TableHead>
                    <StyledTableRow>
                        <StyledTableCell/>
                        {columns.map((column) => (
                            <StyledTableCell key={column.field} sx={{fontSize: 18, fontFamily: 'Segoe UI', fontWeight: 'normal'}}>
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
    );
}

export default Products;