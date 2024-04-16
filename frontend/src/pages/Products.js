import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Card from '@mui/material/Card';
import {alpha, Box, Button, IconButton, InputBase, Stack, styled, tableCellClasses, Typography} from "@mui/material";
import Collapse from '@mui/material/Collapse';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';
import {useEffect, useState} from "react";
import axios from "axios";
import {useAuth} from "../provider/authProvider";
import {Container} from "react-bootstrap";
import Alert from "@mui/material/Alert";
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import ModeEditIcon from '@mui/icons-material/ModeEdit';

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

    function handleUpdate(id) {

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
            fontFamily: 'Segoe UI',
            fontSize: 18,
            fontWeight: 'normal',
        },
        [`&.${tableCellClasses.body}`]: {
            fontFamily: 'Segoe UI',
            fontSize: 16
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

    const StyledButton = styled(Button)({
        borderColor: "#748c8d",
        color: "#748c8d",
        '&:hover': {
            borderColor: "#2d434b",
            color: "#2d434b",
        }
    })

    const SearchIconWrapper = styled('div')(({ theme }) => ({
        padding: theme.spacing(0, 2),
        height: '100%',
        position: 'absolute',
        pointerEvents: 'none',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    }));

    const StyledInputBase = styled(InputBase)(({ theme }) => ({
        color: 'inherit',
        width: '100%',
        '& .MuiInputBase-input': {
            padding: theme.spacing(1, 1, 1, 0),
            paddingLeft: `calc(1em + ${theme.spacing(4)})`,
            transition: theme.transitions.create('width'),
            [theme.breakpoints.up('sm')]: {
                width: '12ch',
                '&:focus': {
                    width: '20ch',
                },
            },
        },
    }));

    const SearchContainer = styled('div')(({ theme }) => ({
        position: 'relative',
        borderRadius: theme.shape.borderRadius,
        backgroundColor: alpha(theme.palette.common.black, 0.15),
        '&:hover': {
            backgroundColor: alpha(theme.palette.common.black, 0.25),
        },
        marginLeft: 0,
        width: '100%',
        [theme.breakpoints.up('sm')]: {
            marginLeft: theme.spacing(1),
            width: 'auto',
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
                        <Button onClick={() => handleUpdate(row.id)}>
                            <ModeEditIcon color='action'/>
                        </Button>
                        <Button onClick={() => handleDelete(row.id)}>
                            <DeleteOutlineOutlinedIcon color="error"/>
                        </Button>
                    </StyledTableCell>}
                </TableRow>
                <TableRow>
                    <StyledTableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={4}>
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

    function ProductsTable() {
        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 600, margin: '30px auto' }}>
                    <Table aria-label="collapsible table">
                        <TableHead>
                            <StyledTableRow>
                                <StyledTableCell/>
                                {columns.map((column) => (
                                    <StyledTableCell key={column.field}>
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
            </React.Fragment>
        )
    }

    return (
        <Container style={{ marginTop: '50px'}}>
            <Box sx={{maxWidth: 600, margin: '0 auto'}}>
                <Stack direction='row' justifyContent='space-between'>
                    {role === 'MANAGER' &&
                        <StyledButton variant="outlined" startIcon={<AddIcon />}>
                            Add
                        </StyledButton>}
                    <SearchContainer>
                        <SearchIconWrapper>
                            <SearchIcon />
                        </SearchIconWrapper>
                        <StyledInputBase
                            placeholder="Search…"
                            inputProps={{ 'aria-label': 'search' }}
                        />
                    </SearchContainer>
                </Stack>
                {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <ProductsTable/>
            </Box>
        </Container>
    );
}

export default Products;