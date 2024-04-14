import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Card from '@mui/material/Card';
import {Box, Button, IconButton, styled, tableCellClasses, Typography} from "@mui/material";
import Collapse from '@mui/material/Collapse';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
import {useEffect, useState} from "react";
import axios from "axios";
import {useAuth} from "../provider/authProvider";
import {Container} from "react-bootstrap";
import Alert from "@mui/material/Alert";

const Products = () => {
    const [products, setProducts] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("");

    const fetchProductsData = async () => {
        const response = await axios.get("http://localhost:8080/api/product")
        setProducts(response.data);
    };

    useEffect(() => {
        fetchProductsData();
    }, []);

    const handleDelete = async (productId) => {
        const response = await axios.delete("http://localhost:8080/api/product/" + productId)
        if (response.data.error) {
            setErrorMessage("Can't delete product that is stored")
            setTimeout(() => setErrorMessage(""), 3500)
        } else {
            if (response.data === true)
                setProducts(products.filter(product => product.id !== productId));
        }
    }

    const clear = e => {
        setErrorMessage("")
    };

    function processProducts(product) {
        return {
            id: product.id,
            name: product.name,
            category: product.category.name,
            characteristics: product.characteristics
        }
    }

    const columns = [
        {field: 'name', headerName: 'Product name', width: 10},
        {field: 'category', headerName: 'Category', width: 10}
    ];

    const rows = products.map((product) => processProducts(product));

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
                            onClick={() => setOpen(!open)}>
                            {open ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}
                        </IconButton>
                    </StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="left">{row.name}</StyledTableCell>
                    <StyledTableCell align="left">{row.category}</StyledTableCell>
                    {role === "MANAGER" && <StyledTableCell align="right">
                        <Button onClick={() => handleDelete(row.id)}>
                            <DeleteOutlineOutlinedIcon color="error"/>
                        </Button>
                    </StyledTableCell>}
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
        <Container>
            {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
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
                            {role === "MANAGER" && <StyledTableCell/>}
                        </StyledTableRow>
                    </TableHead>
                    <TableBody>
                        {rows.map((row) => (
                            <Row key={row.name} row={row} />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Container>
    );
}

export default Products;