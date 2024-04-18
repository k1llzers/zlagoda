import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Card from '@mui/material/Card';
import {
    Box,
    Button, FormControl,
    Dialog, DialogContent,
    IconButton,
    Stack,
    Typography, MenuItem
} from "@mui/material";
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
import StyledTableCell from "../styledComponent/styledTableCell";
import StyledTextField from "../styledComponent/styledTextField";
import StyledButton from "../styledComponent/styldButton";
import StyledTableRow from "../styledComponent/styledTableRow";
import SearchContainer from "../styledComponent/searchContainer";
import SearchIconWrapper from "../styledComponent/searchIconWrapper";
import StyledInputBase from "../styledComponent/styledInputBase";
import CategoryDropdown from "../components/categoriesDropdown";

const Products = () => {
    const [products, setProducts] = useState([])
    const [categories, setCategories] = useState([]);
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("");
    const [openForm, setOpenForm] = useState(false)
    const [updateRow, setUpdateRow] = useState(undefined)
    const [search, setSearch] = useState("")
    const [category, setCategory] = useState(0)

    const fetchProductsData = async () => {
        let response
        if (category === 0)
            response = await axios.get("http://localhost:8080/api/product/order-by/name")
        else
            response = await axios.get("http://localhost:8080/api/product/order-by/name/" + category)
        setProducts(response.data)
    };

    useEffect(() => {
        fetchProductsData();
        fetchCategoryData()
    }, [category]);

    const fetchCategoryData = async () => {
        const response = await axios.get("http://localhost:8080/api/category")
        setCategories(response.data);
    }

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

    function handleUpdate(row) {
        setUpdateRow(row)
        handleOpenForm()
    }

    const fetchSearchingProductsData = async (name) => {
        const response = await axios.get("http://localhost:8080/api/product/by-name/" + name)
        setProducts(response.data);
    };

    const handleSearchChange = async (e) => {
        setSearch(e.target.value)
        setCategory(0)
        if (e.target.value.trim().length > 0)
            await fetchSearchingProductsData(e.target.value)
        else
            await fetchProductsData()
    };

    function handleOpenForm() {
        setOpenForm(true)
        fetchCategoryData()
    }

    function handleClose(){
        setOpenForm(false)
        setUpdateRow(undefined)
    }

    const clear = e => {
        setErrorMessage("")
    };

    function processProducts(product) {
        return {
            id: product.id,
            name: product.name,
            category: product.category.name,
            categoryId: product.category.id,
            characteristics: product.characteristics
        }
    }

    const columns = [
        {field: 'name', headerName: 'Product'},
        {field: 'category', headerName: 'Category'}
    ];

    const rows = products.map((product) => processProducts(product))

    function ProductForm(props) {
        const {onClose, open, row} = props;
        const [name, setName] = useState(row ? row.name : "")
        const [characteristics, setCharacteristics] = useState(row ? row.characteristics : "")
        const [category, setCategory] = useState(row ? row.categoryId : "")
        const [disableAdd, setDisableAdd] = useState(true)

        useEffect(() => {
            if(name.trim() && characteristics.trim() && category && name.length<=50 && characteristics.length<=100) {
                setDisableAdd(false)
            }else {
                setDisableAdd(true)
            }

        }, [name, characteristics, category]);

        const handleAdd = async () => {
            handleClose()
            let response;
            if(!row) {
                response = await axios.post("http://localhost:8080/api/product", {
                    categoryId: +category,
                    name: name,
                    characteristics: characteristics
                })
            } else {
                response = await axios.put("http://localhost:8080/api/product", {
                    id: row.id,
                    categoryId: +category,
                    name: name,
                    characteristics: characteristics
                })
            }
            if (response.data.error) {
                setErrorMessage(response.data.error)
                setTimeout(() => setErrorMessage(""), 3500)
            } else {
                fetchProductsData()
            }
        }

        return (
            <Dialog onClose={onClose} open={open}>
                <DialogContent>
                    <FormControl fullWidth>
                        <StyledTextField id="outlined-basic" label="Name" variant="outlined" value={name}
                                         required
                                         error={name.length > 50}
                                         helperText={name.length > 50 ? "Too long" : ""}
                                         onChange={(event) => {setName(event.target.value)}}/>
                        <StyledTextField
                            id="outlined-multiline-flexible"
                            label="Characteristics"
                            required
                            error={characteristics.length > 100}
                            helperText={characteristics.length > 100 ? "Too long" : ""}
                            multiline
                            value={characteristics}
                            onChange={(event) => {setCharacteristics(event.target.value)}}
                        />
                        <CategoryDropdown
                            required
                            categories={categories}
                            category={category}
                            setCategory={setCategory}
                        />
                        <StyledButton
                            variant="text"
                            sx={{width: '50%', alignSelf: 'center'}}
                            onClick={handleAdd}
                            disabled={disableAdd}
                        >{row ? "Update" : "Add"}</StyledButton>
                    </FormControl>
                </DialogContent>
            </Dialog>
        )
    }


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
                    <StyledTableCell component="th" scope="row " align="center">{row.name}</StyledTableCell>
                    <StyledTableCell align="center">{row.category}</StyledTableCell>
                    {role === "MANAGER" && <StyledTableCell align="right">
                        <Button onClick={() => handleUpdate(row)}>
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
        const width = role === "CASHIER" ? "50%" : "35%"

        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 600, margin: '30px auto' }}>
                    <Table aria-label="collapsible table">
                        <TableHead>
                            <StyledTableRow>
                                <StyledTableCell/>
                                {columns.map((column) => (
                                    <StyledTableCell align="center" key={column.field} sx={{width: {width}}}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                                {role === "MANAGER" && <StyledTableCell sx={{width:"35%"}} />}
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
            <ProductForm
                open={openForm}
                onClose={handleClose}
                row={updateRow}
            />
            <Box sx={{maxWidth: 600, margin: '0 auto'}}>
                <Stack direction='row' justifyContent='space-between'>
                    {role === 'MANAGER' &&
                        <StyledButton variant="outlined"
                                      startIcon={<AddIcon />}
                                      onClick={handleOpenForm}
                                      sx={{maxHeight:'40px', marginTop:'10px'}}
                                          >
                            Add
                        </StyledButton>}
                    <CategoryDropdown
                        sx={{maxHeight:'40px', minWidth:'120px'}}
                        sxChild={{maxHeight:'40px', minWidth:'120px'}}
                        noneOption={true}
                        categories={categories}
                        category={category}
                        setCategory={setCategory}
                    />
                    <SearchContainer sx={{maxHeight:'40px', marginTop:'10px'}}>
                        <SearchIconWrapper>
                            <SearchIcon />
                        </SearchIconWrapper>
                        <StyledInputBase
                            placeholder="Search…"
                            value={search}
                            inputProps={{ 'aria-label': 'search' }}
                            onChange={handleSearchChange}
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